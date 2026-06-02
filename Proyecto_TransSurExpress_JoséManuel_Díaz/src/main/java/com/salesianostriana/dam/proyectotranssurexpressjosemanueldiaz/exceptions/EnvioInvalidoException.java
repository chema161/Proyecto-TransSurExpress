package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;

/**
 * Se lanza cuando un envío no cumple los requisitos mínimos para ser planificado.
 * Por ejemplo: peso inferior al umbral mínimo permitido (0.5 kg).
 */
public class EnvioInvalidoException extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
    public EnvioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
