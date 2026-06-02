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

    @Query("""
            SELECT COALESCE(SUM(ev.envio.peso), 0.0)
            FROM EnvioVehiculo ev
            WHERE ev.vehiculo.id = :vehiculoId
              AND ev.estado IN :estados
            """)
    Double sumarPesoEnviosActivosPorVehiculo(
            @Param("vehiculoId") Long vehiculoId,
            @Param("estados") List<EstadoEnvio> estados
    );

    @Query("""
            SELECT COALESCE(SUM(ev.envio.peso), 0.0)
            FROM EnvioVehiculo ev
            WHERE ev.vehiculo.id = :vehiculoId
              AND ev.estado IN :estados
              AND (:operacionId IS NULL OR ev.id <> :operacionId)
            """)
    Double sumarPesoEnviosActivosPorVehiculoExcluyendoOperacion(
            @Param("vehiculoId") Long vehiculoId,
            @Param("estados") List<EstadoEnvio> estados,
            @Param("operacionId") Long operacionId
    ); 

    // Bloquea crear un tramo duplicado: mismo vehículo + misma fecha + mismo envío.
    // Un vehículo si puede llevar varios envíos distintos el mismo día.

    // Al crear: tramo duplicado exacto 
    boolean existsByVehiculoAndFechaAndEnvioAndEstadoIn(
            Vehiculo vehiculo,
            LocalDate fecha,
            Envio envio,
            List<EstadoEnvio> estados
    );

    // Al editar: tramo duplicado exacto, excluyendo la operación actual 
    boolean existsByVehiculoAndFechaAndEnvioAndEstadoInAndIdNot(
            Vehiculo vehiculo,
            LocalDate fecha,
            Envio envio,
            List<EstadoEnvio> estados,
            Long id
    );

    // Un envío activo solo puede estar asignado a un vehículo a la vez.
    // Ningún otro vehículo puede coger un envío que ya está en curso.

    // Al crear: el envío ya está activo con un vehículo diferente 
    boolean existsByEnvioAndVehiculoNotAndEstadoIn(
            Envio envio,
            Vehiculo vehiculo,
            List<EstadoEnvio> estados
    );

    // Al editar: igual, excluyendo la operación actual 
    boolean existsByEnvioAndVehiculoNotAndEstadoInAndIdNot(
            Envio envio,
            Vehiculo vehiculo,
            List<EstadoEnvio> estados,
            Long id
    );

    // Evita que un conductor esté activo en dos vehículos distintos el mismo día.
    @Query("""
            SELECT CASE WHEN COUNT(ev) > 0 THEN true ELSE false END
            FROM EnvioVehiculo ev
            JOIN ev.vehiculo v
            JOIN v.conductores c
            WHERE c IN :conductores
              AND c.activo = true
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
