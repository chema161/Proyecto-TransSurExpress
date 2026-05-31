package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Conductor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "Los años de experiencia son obligatorios")
    @Min(value = 0, message = "La experiencia no puede ser negativa")
    @Max(value = 50, message = "La experiencia no puede superar los 50 años")
    private Integer experiencia;

    @Builder.Default
    private boolean activo = true;

    @ManyToOne
    private Vehiculo vehiculo;
  
}
