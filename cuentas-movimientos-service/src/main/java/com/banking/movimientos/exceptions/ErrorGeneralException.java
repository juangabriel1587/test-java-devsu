package com.banking.movimientos.exceptions;


public class ErrorGeneralException extends RuntimeException {
    public ErrorGeneralException(String mensaje) {
        super(mensaje);
    }
}
