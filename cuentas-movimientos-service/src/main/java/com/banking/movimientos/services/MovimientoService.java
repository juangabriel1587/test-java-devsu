package com.banking.movimientos.services;


import com.banking.movimientos.exceptions.RecursoNoEncontradoException;
import com.banking.movimientos.exceptions.SaldoInsuficienteException;
import com.banking.movimientos.models.Movimiento;
import com.banking.movimientos.models.TipoMovimiento;
import com.banking.movimientos.repositories.MovimientoRepository;
import com.banking.movimientos.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService implements IMovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public Movimiento registrarMovimiento(Movimiento movimiento) {
        var cuenta = cuentaRepository.findById(movimiento.getCuenta().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + movimiento.getCuenta().getId()));

        // Validar saldo si es un RETIRO
        if (movimiento.getTipoMovimiento() == TipoMovimiento.RETIRO 
            && cuenta.getSaldoInicial().compareTo(movimiento.getValor()) < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Actualizar saldo de la cuenta
        BigDecimal nuevoSaldo = (movimiento.getTipoMovimiento() == TipoMovimiento.DEPOSITO)
                ? cuenta.getSaldoInicial().add(movimiento.getValor())
                : cuenta.getSaldoInicial().subtract(movimiento.getValor());

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // Registrar movimiento con el saldo actualizado
        movimiento.setSaldoDisponible(nuevoSaldo);
        return movimientoRepository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findById(id);
    }
}

