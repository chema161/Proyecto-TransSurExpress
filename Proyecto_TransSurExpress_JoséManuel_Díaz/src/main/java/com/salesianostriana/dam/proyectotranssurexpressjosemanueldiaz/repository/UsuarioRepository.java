package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
