package com.banking.movimientos.exceptions;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(Long clienteId) {
        super("No se encontró información para el cliente con ID: " + clienteId);
    }
}
