package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnvioVehiculoService {
    private final EnvioVehiculoRepository repository;

    public List<EnvioVehiculo> findAll() { return repository.findAll(); }
    public Optional<EnvioVehiculo> findById(Long id) { return repository.findById(id); }
    
    public EnvioVehiculo save(EnvioVehiculo envioVehiculo) {
        return repository.save(envioVehiculo);
    }
    
    public void deleteById(Long id) { repository.deleteById(id); }
}