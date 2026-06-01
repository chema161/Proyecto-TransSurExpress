package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "envio_vehiculo")
public class EnvioVehiculo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debes seleccionar un envío")
    @ManyToOne
    @JoinColumn(name = "envio_id")
    private Envio envio;

    @NotNull(message = "Debes seleccionar un vehículo")
    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private LocalDate fechaEstimadaEntrega;

    @NotBlank(message = "El punto de control es obligatorio")
    @Size(max = 200, message = "El lugar no puede superar los 200 caracteres")
    private String lugar;

    @NotNull(message = "La distancia es obligatoria")
    @DecimalMin(value = "0.01", message = "La distancia debe ser mayor que 0")
    private Double distancia;
}
