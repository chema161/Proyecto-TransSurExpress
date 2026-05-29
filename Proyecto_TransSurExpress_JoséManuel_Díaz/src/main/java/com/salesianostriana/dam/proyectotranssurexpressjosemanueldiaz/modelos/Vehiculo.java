package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vehiculo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(regexp = "^\\d{4}-[A-Za-z]{3}$",
             message = "Formato incorrecto. Usa el formato: 1234-ABC")
    private String matricula;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @DecimalMin(value = "0.01", message = "La capacidad debe ser mayor que 0")
    private Double capacidad;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnvioVehiculo> envios;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conductor> conductores;
}
