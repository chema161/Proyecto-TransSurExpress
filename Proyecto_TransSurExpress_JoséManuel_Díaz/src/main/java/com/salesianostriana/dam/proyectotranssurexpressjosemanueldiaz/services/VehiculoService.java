package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehiculoService extends BaseServiceImpl<Vehiculo, Long, VehiculoRepository> {

    public List<Vehiculo> findByMatricula(String matricula) {
        return repository.findByMatriculaContainingIgnoreCase(matricula);
    }
}
