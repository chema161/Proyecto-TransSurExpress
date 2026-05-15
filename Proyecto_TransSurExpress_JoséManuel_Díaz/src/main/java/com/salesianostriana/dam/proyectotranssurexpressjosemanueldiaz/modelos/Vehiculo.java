package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vehiculo {

	@Id @GeneratedValue
	private Long id;
	private String matricula;
	private Double capacidad;
	
	@OneToMany(mappedBy = "vehiculo")
	private List<EnvioVehiculo> envios;

	@OneToMany(mappedBy = "vehiculo")
	private List<Conductor> conductores;
}
