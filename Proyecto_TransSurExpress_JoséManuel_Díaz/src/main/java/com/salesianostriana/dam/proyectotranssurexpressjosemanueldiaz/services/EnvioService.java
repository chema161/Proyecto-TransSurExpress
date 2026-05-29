package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvioService extends BaseServiceImpl<Envio, Long, EnvioRepository> {

    public List<Envio> findByCodigo(String codigo) {
        return repository.findByCodigoContainingIgnoreCase(codigo);
    }

    public List<Envio> findByOrigenODestino(String texto) {
        return repository.findByOrigenContainingIgnoreCaseOrDestinoContainingIgnoreCase(texto, texto);
    }

    public List<Envio> findPesadosMasQue(Double kg) {
        return repository.findByPesoGreaterThan(kg);
    }
}
