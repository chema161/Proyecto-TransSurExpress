package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;
/**
 * Se lanza cuando se intenta realizar la asignación inicial de un envío a un vehículo 
 *  y no se cumplen las reglas de negocio del sistema:
 * - Si el envío ya está asociado a otra ruta activa.
 * - Si el peso del envío supera la capacidad máxima del vehículo seleccionado.
 * - Si el conductor asignado al vehículo ya tiene otra ruta programada para la misma fecha.
 */
public class AsignacionInvalidaException extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
	
    public AsignacionInvalidaException(String mensaje) { super(mensaje); }
}
