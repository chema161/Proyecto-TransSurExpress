package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;

/**
 * Se lanza cuando se intenta registrar un envío con un código que ya existe en el sistema.
 * El código de envío debe ser único (ej: ENV-2026-001).
 */
public class CodigoEnvioDuplicadoException extends RuntimeException {
	
    public CodigoEnvioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
