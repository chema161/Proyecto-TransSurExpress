package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // <- IMPORTANTE: No olvides este import

@Service
public class OperacionService {

    @Autowired 
    private EnvioVehiculoRepository operacionRepo;
    
    @Autowired 
    private EnvioRepository envioRepo;

    public List<EnvioVehiculo> findAll() { 
        return operacionRepo.findAll(); 
    }

    public Optional<EnvioVehiculo> findById(Long id) {
        return operacionRepo.findById(id);
    }

    public void deleteById(Long id) { 
        operacionRepo.deleteById(id); 
    }

    public void planificarOperacion(EnvioVehiculo op) {
        if (op.getEnvio().getPeso() > op.getVehiculo().getCapacidad()) {
            throw new PesoExcedidoException("El peso del envío (" + op.getEnvio().getPeso() + 
                "kg) excede la capacidad máxima del vehículo (" + op.getVehiculo().getCapacidad() + "kg).");
        }

        if (op.getId() == null) { 
            List<EstadoEnvio> estadosConflictivos = List.of(EstadoEnvio.PREPARADO, EstadoEnvio.EN_RUTA, EstadoEnvio.EN_REPARTO);
            if (operacionRepo.existsByVehiculoAndFechaAndEstadoIn(op.getVehiculo(), op.getFecha(), estadosConflictivos)) {
                throw new AsignacionInvalidaException("El vehículo con matrícula " + op.getVehiculo().getMatricula() + 
                    " ya tiene asignada una ruta activa para la fecha " + op.getFecha() + ".");
            }
        }

        double nuevoCoste = op.getEnvio().getPeso() * op.getDistancia() * 0.02;
        op.getEnvio().setCoste(nuevoCoste); 
        envioRepo.save(op.getEnvio());      

        operacionRepo.save(op);
    }

    public List<EnvioVehiculo> obtenerHistorialPorEnvio(Long envioId) {
        return operacionRepo.findByEnvioIdOrderByFechaAsc(envioId);
    }
}