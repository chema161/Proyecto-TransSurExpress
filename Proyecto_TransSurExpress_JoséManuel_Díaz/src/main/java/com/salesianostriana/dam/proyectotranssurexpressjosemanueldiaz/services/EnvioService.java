package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.CodigoEnvioDuplicadoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.EnvioInvalidoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.HistorialEstadoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvioService extends BaseServiceImpl<Envio, Long, EnvioRepository> {

    private static final double PESO_MINIMO_KG = 0.5;

    @Autowired
    private EnvioVehiculoRepository envioVehiculoRepository;

    @Autowired
    private HistorialEstadoRepository historialRepository;

    /**
     * Guarda un envío aplicando las reglas de negocio:
     *
     * 1. Peso mínimo       → EnvioInvalidoException   (peso < 0.5 kg)
     * 2. Código duplicado  → CodigoEnvioDuplicadoException (solo al crear)
     * 3. Edición segura    → carga la entidad existente para no perder las rutas asociadas.
     *
     * @Transactional es necesario para que findById y save compartan la misma sesión
     * de Hibernate
     */
    @Transactional
    public Envio guardarConValidacion(Envio envio) {

        if (envio.getPeso() != null && envio.getPeso() < PESO_MINIMO_KG) {
            throw new EnvioInvalidoException(
                "El peso del envío (" + envio.getPeso() + " kg) " +
                "es inferior al mínimo permitido de " + PESO_MINIMO_KG + " kg."
            );
        }

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
            return repository.save(envio);
        }

        return repository.findById(envio.getId()).map(existing -> {
            existing.setOrigen(envio.getOrigen());
            existing.setDestino(envio.getDestino());
            existing.setPeso(envio.getPeso());
            existing.setCoste(envio.getCoste());
            return repository.save(existing);
        }).orElse(repository.save(envio));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        List<EnvioVehiculo> operaciones = envioVehiculoRepository.findByEnvioId(id);
        operaciones.forEach(op -> historialRepository.deleteByEnvioVehiculoId(op.getId()));
        repository.deleteById(id);
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