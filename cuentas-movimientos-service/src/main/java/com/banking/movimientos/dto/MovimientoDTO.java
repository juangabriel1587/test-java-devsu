package com.banking.movimientos.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimientoDTO {
    private String fecha;
    private String clienteNombre;
    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private boolean estado;
    private BigDecimal movimiento;
    private BigDecimal saldoDisponible;

    public MovimientoDTO(LocalDateTime fecha, String clienteNombre, String numeroCuenta, String tipo, BigDecimal saldoInicial,
                         boolean estado, BigDecimal movimiento, BigDecimal saldoDisponible) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fecha = fecha.format(formatter);
        this.clienteNombre = clienteNombre;
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    // Getters
    public String getFecha() { return fecha; }
    public String getClienteNombre() { return clienteNombre; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipo() { return tipo; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public boolean isEstado() { return estado; }
    public BigDecimal getMovimiento() { return movimiento; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
}
