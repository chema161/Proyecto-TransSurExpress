package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.base;

import java.util.List;
import java.util.Optional;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;

public interface BaseService<T, ID> {

	List<T> findAll();
	
	Optional<T> findById(ID id);
	
	T save(T t);
	
	T edit(T t);
	
	void delete(T t);
	
	void deleteById(ID id); 
} 
