package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EstadoEnvio;
import java.util.List;

@Repository
public interface EnvioVehiculoRepository extends JpaRepository<EnvioVehiculo, Long> {
    // Consultar rutas por estado
    List<EnvioVehiculo> findByEstado(EstadoEnvio estado);

    // Consultar rutas por envío
    List<EnvioVehiculo> findByEnvioId(Long envioId);

    // Consultar rutas por vehículo
    List<EnvioVehiculo> findByVehiculoId(Long vehiculoId);
}