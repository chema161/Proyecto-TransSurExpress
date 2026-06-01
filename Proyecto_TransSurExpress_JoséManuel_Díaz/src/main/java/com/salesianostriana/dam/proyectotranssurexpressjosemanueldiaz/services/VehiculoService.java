package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EstadoEnvio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VehiculoService extends BaseServiceImpl<Vehiculo, Long, VehiculoRepository> {

    private static final List<EstadoEnvio> ESTADOS_ACTIVOS = List.of(
            EstadoEnvio.PREPARADO,
            EstadoEnvio.EN_RUTA,
            EstadoEnvio.EN_REPARTO
    );

    @Autowired
    private EnvioVehiculoRepository envioVehiculoRepository;

    @Override
    public List<Vehiculo> findAll() {
        return repository.findByActivoTrue();
    }

    public List<Vehiculo> findByMatricula(String matricula) {
        return repository.findByMatriculaContainingIgnoreCaseAndActivoTrue(matricula);
    }

    public List<Vehiculo> findAllIncluyendoInactivos() {
        return repository.findAll();
    }

    @Override
    public Vehiculo save(Vehiculo vehiculo) {
        if (vehiculo.getId() == null) {
            vehiculo.setActivo(true);
        }
        return repository.save(vehiculo);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresent(vehiculo -> {
            vehiculo.setActivo(false);
            repository.save(vehiculo);
        });
    }

    public double calcularCapacidadDisponible(Vehiculo vehiculo) {
        Double pesoAsignado = envioVehiculoRepository.sumarPesoEnviosActivosPorVehiculo(
                vehiculo.getId(), ESTADOS_ACTIVOS);
        return vehiculo.getCapacidad() - (pesoAsignado != null ? pesoAsignado : 0);
    }

    public Map<Long, Double> calcularCapacidadesDisponibles(List<Vehiculo> vehiculos) {
        Map<Long, Double> capacidades = new LinkedHashMap<>();
        vehiculos.forEach(vehiculo ->
                capacidades.put(vehiculo.getId(), calcularCapacidadDisponible(vehiculo)));
        return capacidades;
    }
}
