package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;
/**
 * Se lanza cuando uno o varios envíos superan el limite de peso que puede
 * llevar un vehiculo
 */
public class PesoExcedidoException extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
    public PesoExcedidoException(String mensaje) { super(mensaje); }
}