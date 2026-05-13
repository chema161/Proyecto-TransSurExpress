package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    // Consulta por código
    List<Envio> findByCodigoContainingIgnoreCase(String codigo);

    // Consulta por origen o destino
    List<Envio> findByOrigenContainingIgnoreCaseOrDestinoContainingIgnoreCase(String origen, String destino);

    // Consulta por peso mayor a un valor
    List<Envio> findByPesoGreaterThan(Double peso);
}