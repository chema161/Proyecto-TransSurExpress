package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnvioVehiculoRepository extends JpaRepository<EnvioVehiculo, Long> {

    List<EnvioVehiculo> findByEstado(EstadoEnvio estado);

    List<EnvioVehiculo> findByEnvioId(Long envioId);

    List<EnvioVehiculo> findByVehiculoId(Long vehiculoId);

    List<EnvioVehiculo> findByEnvioIdOrderByFechaAsc(Long envioId);

    /**
     * Comprueba si un vehículo ya tiene asignada una ruta activa en una fecha
     * para un envío DISTINTO al indicado.
     * Permite múltiples tramos del mismo envío en el mismo vehículo y fecha,
     * pero bloquea asignar ese vehículo a un envío diferente ese día.
     */
    boolean existsByVehiculoAndFechaAndEstadoInAndEnvioNot(
            Vehiculo vehiculo,
            LocalDate fecha,
            List<EstadoEnvio> estados,
            Envio envio
    );

    /**
     * Comprueba si alguno de los conductores dados ya tiene una ruta activa
     * a través de un vehículo DIFERENTE al indicado en la misma fecha.
     * Evita que un conductor conduzca dos vehículos distintos el mismo día.
     */
    @Query("""
            SELECT CASE WHEN COUNT(ev) > 0 THEN true ELSE false END
            FROM EnvioVehiculo ev
            JOIN ev.vehiculo v
            JOIN v.conductores c
            WHERE c IN :conductores
              AND ev.vehiculo <> :vehiculo
              AND ev.fecha = :fecha
              AND ev.estado IN :estados
            """)
    boolean existsConductorOcupadoEnOtroVehiculo(
            @Param("conductores") List<Conductor> conductores,
            @Param("vehiculo") Vehiculo vehiculo,
            @Param("fecha") LocalDate fecha,
            @Param("estados") List<EstadoEnvio> estados
    );
}
