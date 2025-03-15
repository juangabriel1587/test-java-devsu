package com.banking.movimientos.services;

import com.banking.movimientos.exceptions.RecursoNoEncontradoException;
import com.banking.movimientos.models.Cuenta;
import com.banking.movimientos.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService implements ICuentaService {

    private final CuentaRepository cuentaRepository;

    @Autowired
    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));
    }

    @Override
    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
    	
    	 return Optional.ofNullable(cuentaRepository.findByNumeroCuenta(numeroCuenta)
 	            .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada Numero: " + numeroCuenta)));
    	 
        //return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuenta.setNumeroCuenta(cuentaActualizada.getNumeroCuenta());
            cuenta.setTipoCuenta(cuentaActualizada.getTipoCuenta());
            cuenta.setSaldoInicial(cuentaActualizada.getSaldoInicial());
            cuenta.setEstado(cuentaActualizada.isEstado());
            return cuentaRepository.save(cuenta);
        }).orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + id));
    }


    @Override
    public void eliminarCuenta(Long id) {
    	 Cuenta cuenta = cuentaRepository.findById(id)
    	            .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

    	
        cuentaRepository.deleteById(id);
    }
}
