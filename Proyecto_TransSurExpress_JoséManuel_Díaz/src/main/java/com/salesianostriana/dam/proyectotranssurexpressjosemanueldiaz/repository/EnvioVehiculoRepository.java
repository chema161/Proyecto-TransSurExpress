package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnvioVehiculoRepository extends JpaRepository<EnvioVehiculo, Long> {

    List<EnvioVehiculo> findByEstado(EstadoEnvio estado);

    List<EnvioVehiculo> findByEnvioId(Long envioId);

    List<EnvioVehiculo> findByVehiculoId(Long vehiculoId);
    
    boolean existsByVehiculoAndFechaAndEstadoIn(Vehiculo vehiculo, LocalDate fecha, List<EstadoEnvio> estados);
    
    List<EnvioVehiculo> findByEnvioIdOrderByFechaAsc(Long envioId);
}