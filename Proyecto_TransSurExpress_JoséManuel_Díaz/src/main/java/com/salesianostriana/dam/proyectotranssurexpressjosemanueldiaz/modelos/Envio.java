package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Envio {

	@Id @GeneratedValue
	private Long id;
	private String codigo;
	private String origen;
	private String destino;
	private Double peso;
	private Double coste;
	
	@OneToMany(mappedBy = "envio")
	private List<EnvioVehiculo> rutas; 
	
}
