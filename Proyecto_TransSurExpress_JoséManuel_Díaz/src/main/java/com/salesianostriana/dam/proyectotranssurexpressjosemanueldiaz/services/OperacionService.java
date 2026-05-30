package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperacionService extends BaseServiceImpl<EnvioVehiculo, Long, EnvioVehiculoRepository> {

    /** Peso mínimo en kg que debe tener un envío para poder planificarse */
    private static final double PESO_MINIMO_KG = 0.5;

    /** Tarifa base: coste = peso (kg) × distancia (km) × TARIFA */
    private static final double TARIFA_POR_KG_KM = 0.02;

    @Autowired
    private EnvioRepository envioRepo;

    /**
     * Aplica todas las reglas de negocio antes de guardar una operación:
     *
     * 1. Peso mínimo       → EnvioInvalidoException   (peso < 0.5 kg)
     * 2. Peso máximo       → PesoExcedidoException    (peso > capacidad vehículo)
     * 3. Sin solapamientos → AsignacionInvalidaException (mismo vehículo, misma fecha, DISTINTO envío)
     * 4. Conductor libre   → ConductorOcupadoException (conductor ya activo en otro vehículo ese día)
     * 5. Cálculo del coste → automático: peso × distancia × 0,02 €
     */
    public void planificarOperacion(EnvioVehiculo op) {

        List<EstadoEnvio> estadosActivos = List.of(
                EstadoEnvio.PREPARADO,
                EstadoEnvio.EN_RUTA,
                EstadoEnvio.EN_REPARTO
        );

        // 1. Bloqueo por peso mínimo 
        double peso = op.getEnvio().getPeso();
        if (peso < PESO_MINIMO_KG) {
            throw new EnvioInvalidoException(
                    "El envío no puede planificarse: su peso (" + peso + " kg) " +
                    "es inferior al mínimo permitido de " + PESO_MINIMO_KG + " kg."
            );
        }

        // 2. Control de peso máximo (capacidad del vehículo) 
        double capacidad = op.getVehiculo().getCapacidad();
        if (peso > capacidad) {
            throw new PesoExcedidoException(
                    "El peso del envío (" + peso + " kg) excede la capacidad " +
                    "máxima del vehículo " + op.getVehiculo().getMatricula() +
                    " (" + capacidad + " kg)."
            );
        }

        // ── 3. Sin solapamientos (mismo vehículo, misma fecha, DISTINTO envío) ─
        // Permite múltiples tramos del mismo envío en el mismo vehículo y fecha.
        // Bloquea si ese vehículo ya está ocupado con un envío diferente ese día.
        if (repository.existsByVehiculoAndFechaAndEstadoInAndEnvioNot(
                op.getVehiculo(), op.getFecha(), estadosActivos, op.getEnvio())) {
            throw new AsignacionInvalidaException(
                    "El vehículo " + op.getVehiculo().getMatricula() +
                    " ya tiene asignada una ruta activa para otro envío " +
                    "en la fecha " + op.getFecha() + "."
            );
        }

        // 4. Conductor disponible 
        // Comprueba que ningún conductor del vehículo esté activo en OTRO vehículo ese día.
        List<Conductor> conductores = op.getVehiculo().getConductores();
        if (conductores != null && !conductores.isEmpty()) {
            if (repository.existsConductorOcupadoEnOtroVehiculo(
                    conductores, op.getVehiculo(), op.getFecha(), estadosActivos)) {

                // Obtenemos el nombre del primer conductor para el mensaje
                String nombreConductor = conductores.get(0).getNombre();
                throw new ConductorOcupadoException(
                        "El conductor " + nombreConductor +
                        " ya está activo en otra ruta el " + op.getFecha() +
                        ". No puede conducir dos vehículos el mismo día."
                );
            }
        }

        //  5. Cálculo automático del coste 
        // coste = peso (kg) × distancia (km) × 0,02 €
        double nuevoCoste = peso * op.getDistancia() * TARIFA_POR_KG_KM;
        op.getEnvio().setCoste(nuevoCoste);
        envioRepo.save(op.getEnvio());

        repository.save(op);
    }

    public List<EnvioVehiculo> obtenerHistorialPorEnvio(Long envioId) {
        return repository.findByEnvioIdOrderByFechaAsc(envioId);
    }
}
