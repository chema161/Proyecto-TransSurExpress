package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.ConductorRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConductorService extends BaseServiceImpl<Conductor, Long, ConductorRepository> {

    public List<Conductor> findByNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Conductor> findConExperiencia(Integer anios) {
        return repository.findByExperienciaGreaterThan(anios);
    }
}
