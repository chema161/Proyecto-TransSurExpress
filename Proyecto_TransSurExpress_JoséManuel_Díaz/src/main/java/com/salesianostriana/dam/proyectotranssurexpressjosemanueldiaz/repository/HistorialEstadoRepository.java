package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByEnvioVehiculoIdOrderByFechaHoraAsc(Long envioVehiculoId);
}
