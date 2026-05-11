package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import java.util.List;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    // Buscar por nombre
    List<Conductor> findByNombreContainingIgnoreCase(String nombre);

    // Buscar conductores con experiencia mayor a X años
    List<Conductor> findByExperienciaGreaterThan(Integer experiencia);
}