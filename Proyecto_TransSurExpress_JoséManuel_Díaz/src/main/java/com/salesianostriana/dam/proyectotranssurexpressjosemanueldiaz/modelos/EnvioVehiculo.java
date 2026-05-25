package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "envio_vehiculo")
public class EnvioVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "envio_id")
    private Envio envio;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado; 

    private LocalDate fecha;  
    private String lugar;    
    private Double distancia; 
}