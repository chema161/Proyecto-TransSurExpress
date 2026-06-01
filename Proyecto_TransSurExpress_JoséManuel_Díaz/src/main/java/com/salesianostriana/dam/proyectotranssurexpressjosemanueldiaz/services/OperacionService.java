package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.HistorialEstadoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperacionService extends BaseServiceImpl<EnvioVehiculo, Long, EnvioVehiculoRepository> {

    private static final double PESO_MINIMO_KG   = 0.5;
    private static final double TARIFA_POR_KG_KM = 0.02;
    private static final double KM_POR_DIA_ESTIMADO = 300.0;

    @Autowired
    private EnvioRepository envioRepo;

    @Autowired
    private HistorialEstadoRepository historialRepo;

    @Autowired
    private VehiculoRepository vehiculoRepo;

    /**
     * Aplica todas las reglas de negocio antes de guardar una operación
     * y registra el cambio de estado en el historial:
     *
     * 1. Peso mínimo           → EnvioInvalidoException
     * 2. Peso máximo           → PesoExcedidoException
     * 3. Sin tramos duplicados → AsignacionInvalidaException
     * 4. Exclusividad envío    → AsignacionInvalidaException
     * 5. Conductor disponible  → ConductorOcupadoException
     * 6. Cálculo del coste     → automático: peso × distancia × 0,02 €
     * 7. Registro de historial → guarda estadoAnterior → estadoNuevo + usuario + timestamp
     */
    public void planificarOperacion(EnvioVehiculo op, String usuario) {

        List<EstadoEnvio> estadosActivos = List.of(
                EstadoEnvio.PREPARADO,
                EstadoEnvio.EN_RUTA,
                EstadoEnvio.EN_REPARTO
        );

        double peso      = op.getEnvio().getPeso();
        double capacidad = op.getVehiculo().getCapacidad();
        boolean esNueva  = (op.getId() == null);

        if (!op.getVehiculo().isActivo()) {
            throw new AsignacionInvalidaException(
                "El vehiculo " + op.getVehiculo().getMatricula() +
                " esta dado de baja y no puede asignarse a una operacion.");
        }

        EstadoEnvio estadoAnterior = null;
        if (!esNueva) {
            estadoAnterior = repository.findById(op.getId())
                    .map(EnvioVehiculo::getEstado)
                    .orElse(null);
        }

        // 1. Peso mínimo 
        if (peso < PESO_MINIMO_KG) {
            throw new EnvioInvalidoException(
                "El envío no puede planificarse: su peso (" + peso + " kg) " +
                "es inferior al mínimo permitido de " + PESO_MINIMO_KG + " kg."
            );
        }

        // 2. Peso máximo (capacidad del vehículo) 
        double pesoAsignado = repository.sumarPesoEnviosActivosPorVehiculoExcluyendoOperacion(
                op.getVehiculo().getId(), estadosActivos, op.getId());
        double capacidadDisponible = capacidad - pesoAsignado;

        if (peso > capacidadDisponible) {
            throw new PesoExcedidoException(
                "El peso del envío (" + peso + " kg) excede la capacidad " +
                "máxima del vehículo " + op.getVehiculo().getMatricula() +
                " (" + capacidadDisponible + " kg disponibles de " + capacidad + " kg)."
            );
        }

        // 3. Sin tramos duplicados 
        boolean tramoDuplicado = esNueva
            ? repository.existsByVehiculoAndFechaAndEnvioAndEstadoIn(
                    op.getVehiculo(), op.getFecha(), op.getEnvio(), estadosActivos)
            : repository.existsByVehiculoAndFechaAndEnvioAndEstadoInAndIdNot(
                    op.getVehiculo(), op.getFecha(), op.getEnvio(), estadosActivos, op.getId());

        if (tramoDuplicado) {
            throw new AsignacionInvalidaException(
                "El vehículo " + op.getVehiculo().getMatricula() +
                " ya tiene registrado un tramo activo para el envío " +
                op.getEnvio().getCodigo() + " en la fecha " + op.getFecha() + "."
            );
        }

        // 4. Exclusividad del envío
        boolean envioOcupado = esNueva
            ? repository.existsByEnvioAndVehiculoNotAndEstadoIn(
                    op.getEnvio(), op.getVehiculo(), estadosActivos)
            : repository.existsByEnvioAndVehiculoNotAndEstadoInAndIdNot(
                    op.getEnvio(), op.getVehiculo(), estadosActivos, op.getId());

        if (envioOcupado) {
            throw new AsignacionInvalidaException(
                "El envío " + op.getEnvio().getCodigo() +
                " ya está siendo gestionado por otro vehículo. " +
                "Un envío activo no puede asignarse a dos vehículos a la vez."
            );
        }

        // 5. Conductor disponible 
        List<Conductor> conductores = op.getVehiculo().getConductores();
        if (conductores != null && !conductores.isEmpty()) {
            if (repository.existsConductorOcupadoEnOtroVehiculo(
                    conductores, op.getVehiculo(), op.getFecha(), estadosActivos)) {
                String nombre = conductores.get(0).getNombre();
                throw new ConductorOcupadoException(
                    "El conductor " + nombre +
                    " ya está activo en otra ruta el " + op.getFecha() +
                    ". No puede conducir dos vehículos el mismo día."
                );
            }
        }

        // 6. Cálculo automático del coste 
        double nuevoCoste = peso * op.getDistancia() * TARIFA_POR_KG_KM;
        op.getEnvio().setCoste(nuevoCoste);
        envioRepo.save(op.getEnvio());

        op.setFechaEstimadaEntrega(calcularFechaEstimada(op.getFecha(), op.getDistancia(), op.getEstado()));

        EnvioVehiculo guardada = repository.save(op);

        // 7. Registro de historial de estado 
        boolean estadoCambio = esNueva || (estadoAnterior != op.getEstado());
        if (estadoCambio) {
            HistorialEstado entrada = HistorialEstado.builder()
                    .envioVehiculo(guardada)
                    .estadoAnterior(estadoAnterior)
                    .estadoNuevo(op.getEstado())
                    .fechaHora(LocalDateTime.now())
                    .usuario(usuario)
                    .build();
            historialRepo.save(entrada);
        }
    }

    /**
     * Reasigna el vehículo de una operación en curso aplicando todas las
     * validaciones necesarias y dejando trazabilidad en el historial de estados.
     *
     * Validaciones:
     * 1. La operación debe existir
     * 2. No se puede reasignar si ya está ENTREGADA
     * 3. El nuevo vehículo debe ser distinto del actual
     * 4. El peso del envío no puede superar la capacidad del nuevo vehículo
     * 5. El conductor del nuevo vehículo no puede estar ocupado ese día
     */
    public void reasignarVehiculo(Long operacionId, Long nuevoVehiculoId, String usuario) {

        // 1. Operación debe existir 
        EnvioVehiculo op = repository.findById(operacionId)
                .orElseThrow(() -> new ReasignacionInvalidaException(
                        "No se encontró la operación con ID " + operacionId + "."));

        // 2. No se puede reasignar si ya está entregada 
        if (op.getEstado() == EstadoEnvio.ENTREGADO) {
            throw new ReasignacionInvalidaException(
                "No se puede reasignar el vehículo de un envío ya entregado.");
        }

        // 3. El nuevo vehículo debe ser distinto del actual 
        if (op.getVehiculo().getId().equals(nuevoVehiculoId)) {
            throw new ReasignacionInvalidaException(
                "El vehículo seleccionado ya está asignado a esta operación.");
        }

        Vehiculo nuevoVehiculo = vehiculoRepo.findById(nuevoVehiculoId)
                .orElseThrow(() -> new ReasignacionInvalidaException(
                        "No se encontró el vehículo con ID " + nuevoVehiculoId + "."));

        // 4. El peso del envío no puede superar la capacidad del nuevo vehículo 
        if (!nuevoVehiculo.isActivo()) {
            throw new ReasignacionInvalidaException(
                "El vehiculo " + nuevoVehiculo.getMatricula() +
                " esta dado de baja y no puede recibir nuevas operaciones.");
        }

        double peso = op.getEnvio().getPeso();
        List<EstadoEnvio> estadosActivos = List.of(
                EstadoEnvio.PREPARADO, EstadoEnvio.EN_RUTA, EstadoEnvio.EN_REPARTO);

        double pesoAsignado = repository.sumarPesoEnviosActivosPorVehiculoExcluyendoOperacion(
                nuevoVehiculo.getId(), estadosActivos, op.getId());
        double capacidadDisponible = nuevoVehiculo.getCapacidad() - pesoAsignado;

        if (peso > capacidadDisponible) {
            throw new ReasignacionInvalidaException(
                "El peso del envío (" + peso + " kg) excede la capacidad " +
                "máxima del nuevo vehículo " + nuevoVehiculo.getMatricula() +
                " (" + capacidadDisponible + " kg disponibles de " + nuevoVehiculo.getCapacidad() + " kg).");
        }

        // 5. El conductor del nuevo vehículo no puede estar ocupado ese día 
        List<Conductor> conductoresNuevo = nuevoVehiculo.getConductores();
        if (conductoresNuevo != null && !conductoresNuevo.isEmpty()) {
            if (repository.existsConductorOcupadoEnOtroVehiculo(
                    conductoresNuevo, nuevoVehiculo, op.getFecha(), estadosActivos)) {
                String nombre = conductoresNuevo.get(0).getNombre();
                throw new ReasignacionInvalidaException(
                    "El conductor " + nombre + " del nuevo vehículo ya está activo " +
                    "en otra ruta el " + op.getFecha() + ".");
            }
        }

        // Reasignación 
        Vehiculo vehiculoAnterior = op.getVehiculo();
        op.setVehiculo(nuevoVehiculo);
        op.setFechaEstimadaEntrega(calcularFechaEstimada(op.getFecha(), op.getDistancia(), op.getEstado()));
        repository.save(op);

        // Registrar en historial con nota de reasignación para trazabilidad
        HistorialEstado entrada = HistorialEstado.builder()
                .envioVehiculo(op)
                .estadoAnterior(op.getEstado())
                .estadoNuevo(op.getEstado())
                .fechaHora(LocalDateTime.now())
                .usuario(usuario + " [reasignación: " +
                         vehiculoAnterior.getMatricula() + " → " +
                         nuevoVehiculo.getMatricula() + "]")
                .build();
        historialRepo.save(entrada);
    }

    public List<EnvioVehiculo> obtenerHistorialPorEnvio(Long envioId) {
        return repository.findByEnvioIdOrderByFechaAsc(envioId);
    }

    public List<HistorialEstado> obtenerHistorialEstadosPorOperacion(Long envioVehiculoId) {
        return historialRepo.findByEnvioVehiculoIdOrderByFechaHoraAsc(envioVehiculoId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        historialRepo.deleteByEnvioVehiculoId(id);
        super.deleteById(id);
    }

    private LocalDate calcularFechaEstimada(LocalDate fechaInicio, Double distancia, EstadoEnvio estado) {
        if (fechaInicio == null || distancia == null) {
            return null;
        }
        if (estado == EstadoEnvio.ENTREGADO) {
            return fechaInicio;
        }
        long dias = Math.max(1, (long) Math.ceil(distancia / KM_POR_DIA_ESTIMADO));
        return fechaInicio.plusDays(dias);
    }
}
