package com.banking.movimientos.services;


import com.banking.movimientos.models.Cuenta;

import java.util.List;
import java.util.Optional;

public interface ICuentaService {

    List<Cuenta> obtenerTodasLasCuentas();

    Cuenta obtenerCuentaPorId(Long id);

    Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta);

    Cuenta crearCuenta(Cuenta cuenta);

    Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada);

    void eliminarCuenta(Long id);
}
