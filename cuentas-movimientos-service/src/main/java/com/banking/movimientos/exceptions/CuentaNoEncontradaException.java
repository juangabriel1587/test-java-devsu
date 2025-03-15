package com.banking.movimientos.exceptions;

public class CuentaNoEncontradaException extends RuntimeException {
    public CuentaNoEncontradaException(Long clienteId) {
        super("No se encontraron cuentas asociadas al cliente con ID: " + clienteId);
    }
}
