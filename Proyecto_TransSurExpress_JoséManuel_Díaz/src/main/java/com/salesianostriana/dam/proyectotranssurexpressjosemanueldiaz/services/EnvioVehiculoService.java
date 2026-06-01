package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EstadoEnvio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvioVehiculoService extends BaseServiceImpl<EnvioVehiculo, Long, EnvioVehiculoRepository> {

    public List<EnvioVehiculo> findByEstado(EstadoEnvio estado) {
        return repository.findByEstado(estado);
    }

    public List<EnvioVehiculo> findByEnvioId(Long envioId) {
        return repository.findByEnvioId(envioId);
    }

    public List<EnvioVehiculo> findByVehiculoId(Long vehiculoId) {
        return repository.findByVehiculoId(vehiculoId);
    }
}
