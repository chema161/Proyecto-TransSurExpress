package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnvioService {
    private final EnvioRepository repository;

    public List<Envio> findAll() { return repository.findAll(); }
    public Optional<Envio> findById(Long id) { return repository.findById(id); }
    
    public Envio save(Envio envio) { 
        return repository.save(envio); 
    }
    
    public void deleteById(Long id) { repository.deleteById(id); }
}
