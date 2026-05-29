package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.CodigoEnvioDuplicadoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvioService extends BaseServiceImpl<Envio, Long, EnvioRepository> {

    /**
     * Guarda un envío comprobando antes que el código no esté duplicado.
     * Solo aplica al crear (id == null); al editar el código es de solo lectura.
     *
     * @throws CodigoEnvioDuplicadoException si ya existe un envío con ese código
     */
    public Envio guardarConValidacion(Envio envio) {
        if (envio.getId() == null) {
            List<Envio> existentes = repository.findByCodigoContainingIgnoreCase(envio.getCodigo());
            boolean codigoDuplicado = existentes.stream()
                .anyMatch(e -> e.getCodigo().equalsIgnoreCase(envio.getCodigo()));
            if (codigoDuplicado) {
                throw new CodigoEnvioDuplicadoException(
                    "Ya existe un envío registrado con el código '" + envio.getCodigo() + "'. " +
                    "Los códigos de envío deben ser únicos."
                );
            }
        }
        return repository.save(envio);
    }

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
