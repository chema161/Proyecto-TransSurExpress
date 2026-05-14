package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements BaseService<T, ID> {

	
}
