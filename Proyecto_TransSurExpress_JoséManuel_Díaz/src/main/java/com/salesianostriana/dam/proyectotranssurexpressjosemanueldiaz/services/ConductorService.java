package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.ConductorRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConductorService extends BaseServiceImpl<Conductor, Long, ConductorRepository> {

    @Override
    public List<Conductor> findAll() {
        return repository.findByActivoTrue();
    }

    public List<Conductor> findByNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    public List<Conductor> findConExperiencia(Integer anios) {
        return repository.findByExperienciaGreaterThanAndActivoTrue(anios);
    }

    public List<Conductor> findAllIncluyendoInactivos() {
        return repository.findAll();
    }

    @Override
    public Conductor save(Conductor conductor) {
        if (conductor.getId() == null) {
            conductor.setActivo(true);
        }
        return repository.save(conductor);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresent(conductor -> {
            conductor.setActivo(false);
            repository.save(conductor);
        });
    }
}
