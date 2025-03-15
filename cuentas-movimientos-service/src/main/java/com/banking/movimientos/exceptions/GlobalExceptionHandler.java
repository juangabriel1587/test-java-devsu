package com.banking.movimientos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Error: Recurso no encontrado
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuestaError("Recurso no encontrado", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Error: Saldo insuficiente
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> manejarSaldoInsuficiente(SaldoInsuficienteException ex) {
        return construirRespuestaError("Saldo insuficiente", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Manejo de validaciones fallidas (ejemplo: @NotNull, @Size en entidades)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validación fallida");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        response.put("details", errores);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Error general (cualquier otro error inesperado)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
        return construirRespuestaError("Error interno del servidor", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar para construir respuestas de error
    private ResponseEntity<Map<String, Object>> construirRespuestaError(String error, String mensaje, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", mensaje);
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        return new ResponseEntity<>(response, status);
    }
}
