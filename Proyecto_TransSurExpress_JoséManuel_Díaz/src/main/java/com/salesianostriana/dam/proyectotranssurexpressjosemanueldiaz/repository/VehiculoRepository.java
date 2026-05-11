package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Buscar vehículo por matrícula
    List<Vehiculo> findByMatriculaContainingIgnoreCase(String matricula);
}
