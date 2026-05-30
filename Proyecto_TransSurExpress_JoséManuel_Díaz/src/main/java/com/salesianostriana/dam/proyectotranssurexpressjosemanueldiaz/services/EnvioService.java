package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.CodigoEnvioDuplicadoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.EnvioInvalidoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioService extends BaseServiceImpl<Envio, Long, EnvioRepository> {

    /** Peso mínimo en kg que debe tener un envío para poder registrarse */
    private static final double PESO_MINIMO_KG = 0.5;

    /**
     * Guarda un envío aplicando las reglas de negocio:
     *
     * 1. Peso mínimo       → EnvioInvalidoException   (peso < 0.5 kg)
     * 2. Código duplicado  → CodigoEnvioDuplicadoException (solo al crear)
     */
    public Envio guardarConValidacion(Envio envio) {

        // ── 1. Bloqueo por peso mínimo ────────────────────────────────────────
        if (envio.getPeso() != null && envio.getPeso() < PESO_MINIMO_KG) {
            throw new EnvioInvalidoException(
                "El peso del envío (" + envio.getPeso() + " kg) " +
                "es inferior al mínimo permitido de " + PESO_MINIMO_KG + " kg."
            );
        }

        // ── 2. Código duplicado (solo al crear) ───────────────────────────────
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
