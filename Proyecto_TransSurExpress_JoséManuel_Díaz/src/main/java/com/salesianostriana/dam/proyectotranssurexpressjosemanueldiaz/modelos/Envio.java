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
public class Envio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código de envío es obligatorio")
    @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
    private String codigo;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 100, message = "El origen no puede superar los 100 caracteres")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 100, message = "El destino no puede superar los 100 caracteres")
    private String destino;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor que 0")
    private Double peso;

    @NotNull(message = "El coste es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El coste no puede ser negativo")
    private Double coste;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnvioVehiculo> rutas;
}
