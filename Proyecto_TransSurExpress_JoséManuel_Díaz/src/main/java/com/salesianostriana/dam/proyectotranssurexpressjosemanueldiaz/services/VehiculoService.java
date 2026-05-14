package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import org.springframework.stereotype.Service;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository repository;

    public List<Vehiculo> findAll() { return repository.findAll(); }
    public Optional<Vehiculo> findById(Long id) { return repository.findById(id); }
    public Vehiculo save(Vehiculo vehiculo) { return repository.save(vehiculo); }
    public void deleteById(Long id) { repository.deleteById(id); }
}