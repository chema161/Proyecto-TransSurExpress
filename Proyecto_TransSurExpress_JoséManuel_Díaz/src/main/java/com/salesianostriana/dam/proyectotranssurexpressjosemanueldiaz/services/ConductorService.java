package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import org.springframework.stereotype.Service;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional; 

@Service
@RequiredArgsConstructor
public class ConductorService {
    private final ConductorRepository repository;

    public List<Conductor> findAll() { return repository.findAll(); }
    public Optional<Conductor> findById(Long id) { return repository.findById(id); }
    public Conductor save(Conductor conductor) { return repository.save(conductor); }
    public void deleteById(Long id) { repository.deleteById(id); }
} 