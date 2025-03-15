package com.banking.movimientos.services;


import com.banking.movimientos.models.Movimiento;

import java.util.List;
import java.util.Optional;

public interface IMovimientoService {

    List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId);

    Movimiento registrarMovimiento(Movimiento movimiento);

    Optional<Movimiento> obtenerMovimientoPorId(Long id);
}
