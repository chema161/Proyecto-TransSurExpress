package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class EnvioVehiculo {

	private Long id;
	private LocalDateTime fecha;
	private String lugar;
	
	@ManyToOne
	private Envio envio;

	@ManyToOne 
	private Vehiculo vehiculo;
	
	@Enumerated(EnumType.STRING)
	private EstadoEnvio estado; 
}
