package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions;

/**
 * Se lanza cuando se intenta reasignar el vehículo de una operación
 * y la operación no cumple las condiciones para ello:
 * - operación ya entregada
 * - mismo vehículo que el actual
 * - peso del envío excede la capacidad del nuevo vehículo
 * - conductor del nuevo vehículo ya ocupado ese día
 */
public class ReasignacionInvalidaException extends RuntimeException {
    public ReasignacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
