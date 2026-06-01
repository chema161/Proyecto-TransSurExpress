package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;

/**
 * Se lanza cuando se intenta asignar un vehículo cuyo conductor
 * ya está activo en otra ruta diferente en la misma fecha.
 */
public class ConductorOcupadoException extends RuntimeException {
	
    public ConductorOcupadoException(String mensaje) {
        super(mensaje);
    }
}
