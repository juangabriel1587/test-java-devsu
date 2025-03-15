package com.banking.movimientos.dto;

import java.math.BigDecimal;
import java.util.List;

public class EstadoCuentaDTO {
    private Long clienteId;
    private String numeroCuenta;
    private BigDecimal saldoActual;
    private List<MovimientoDTO> movimientos;

    // ✅ No-argument constructor (needed for serialization)
    public EstadoCuentaDTO() {}

    // ✅ Constructor with all arguments
    public EstadoCuentaDTO(Long clienteId, String numeroCuenta, BigDecimal saldoActual, List<MovimientoDTO> movimientos) {
        this.clienteId = clienteId;
        this.numeroCuenta = numeroCuenta;
        this.saldoActual = saldoActual;
        this.movimientos = movimientos;
    }

    // ✅ Getters and setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public BigDecimal getSaldoActual() { return saldoActual; }
    public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }

    public List<MovimientoDTO> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoDTO> movimientos) { this.movimientos = movimientos; }
}
