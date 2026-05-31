package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.HistorialEstadoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperacionService extends BaseServiceImpl<EnvioVehiculo, Long, EnvioVehiculoRepository> {

    private static final double PESO_MINIMO_KG   = 0.5;
    private static final double TARIFA_POR_KG_KM = 0.02;

    @Autowired
    private EnvioRepository envioRepo;

    @Autowired
    private HistorialEstadoRepository historialRepo;

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

        // Guardamos el estado anterior antes de cualquier cambio (null si es nueva)
        EstadoEnvio estadoAnterior = null;
        if (!esNueva) {
            estadoAnterior = repository.findById(op.getId())
                    .map(EnvioVehiculo::getEstado)
                    .orElse(null);
        }

        // ── 1. Peso mínimo ────────────────────────────────────────────────────
        if (peso < PESO_MINIMO_KG) {
            throw new EnvioInvalidoException(
                "El envío no puede planificarse: su peso (" + peso + " kg) " +
                "es inferior al mínimo permitido de " + PESO_MINIMO_KG + " kg."
            );
        }

        // ── 2. Peso máximo (capacidad del vehículo) ───────────────────────────
        if (peso > capacidad) {
            throw new PesoExcedidoException(
                "El peso del envío (" + peso + " kg) excede la capacidad " +
                "máxima del vehículo " + op.getVehiculo().getMatricula() +
                " (" + capacidad + " kg)."
            );
        }

        // ── 3. Sin tramos duplicados ──────────────────────────────────────────
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

        // ── 4. Exclusividad del envío ─────────────────────────────────────────
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

        // ── 5. Conductor disponible ───────────────────────────────────────────
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

        // ── 6. Cálculo automático del coste ───────────────────────────────────
        double nuevoCoste = peso * op.getDistancia() * TARIFA_POR_KG_KM;
        op.getEnvio().setCoste(nuevoCoste);
        envioRepo.save(op.getEnvio());

        EnvioVehiculo guardada = repository.save(op);

        // ── 7. Registro de historial de estado ────────────────────────────────
        // Solo registramos si es nueva operación O si el estado ha cambiado
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

    public List<EnvioVehiculo> obtenerHistorialPorEnvio(Long envioId) {
        return repository.findByEnvioIdOrderByFechaAsc(envioId);
    }

    public List<HistorialEstado> obtenerHistorialEstadosPorOperacion(Long envioVehiculoId) {
        return historialRepo.findByEnvioVehiculoIdOrderByFechaHoraAsc(envioVehiculoId);
    }
}
