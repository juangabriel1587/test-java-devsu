package com.banking.movimientos.services;

import com.banking.movimientos.dto.MovimientoDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReporteService {

    /**
     * Genera un reporte del estado de cuenta de un cliente en un rango de fechas.
     * 
     * @param clienteId ID del cliente.
     * @param fechaInicio Fecha inicial del rango.
     * @param fechaFin Fecha final del rango.
     * @return Lista de MovimientoDTO con información de cuentas, cliente y transacciones.
     */
    List<MovimientoDTO> generarReporteEstadoCuenta(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}
