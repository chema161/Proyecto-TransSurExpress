package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.HistorialEstado;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.HistorialEstadoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialEstadoService extends BaseServiceImpl<HistorialEstado, Long, HistorialEstadoRepository> {

    public List<HistorialEstado> findByOperacion(Long envioVehiculoId) {
        return repository.findByEnvioVehiculoIdOrderByFechaHoraAsc(envioVehiculoId);
    }
}
