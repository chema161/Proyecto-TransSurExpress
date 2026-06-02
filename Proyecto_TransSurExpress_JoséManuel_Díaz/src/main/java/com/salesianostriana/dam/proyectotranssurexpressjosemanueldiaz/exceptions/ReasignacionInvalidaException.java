package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;

/**
 * Se lanza cuando se intenta reasignar el vehículo de una operación
 * y la operación no cumple las condiciones para ello:
 * - Si la operación ya esta entregada
 * - Si es el mismo vehículo que el actual
 * - Si el peso del envío excede la capacidad del nuevo vehículo
 * - Si el conductor del nuevo vehículo ya ocupado ese día
 */
public class ReasignacionInvalidaException extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
    public ReasignacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
