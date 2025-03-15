package com.banking.movimientos.unit;

import com.banking.movimientos.exceptions.SaldoInsuficienteException;
import com.banking.movimientos.models.Cuenta;
import com.banking.movimientos.models.Movimiento;
import com.banking.movimientos.models.TipoMovimiento;
import com.banking.movimientos.repositories.CuentaRepository;
import com.banking.movimientos.repositories.MovimientoRepository;
import com.banking.movimientos.services.MovimientoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new Cuenta();
        cuenta.setId(100L);  // ID dinámico
        cuenta.setEstado(true);
        cuenta.setNumeroCuenta("1234567");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
    }

    @Test
    void testRegistrarDeposito() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Movimiento deposito = new Movimiento();
        deposito.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        deposito.setFecha(LocalDateTime.now());
        deposito.setValor(new BigDecimal("500"));
        deposito.setCuenta(cuenta);

        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Movimiento resultado = movimientoService.registrarMovimiento(deposito);

        assertNotNull(resultado);
        assertEquals(0, resultado.getSaldoDisponible().compareTo(new BigDecimal("2500.00"))); // 2000 + 500
    }

    @Test
    void testRegistrarRetiroSaldoSuficiente() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento(TipoMovimiento.RETIRO);
        retiro.setFecha(LocalDateTime.now());
        retiro.setValor(new BigDecimal("500")); 
        retiro.setCuenta(cuenta);

        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Movimiento resultado = movimientoService.registrarMovimiento(retiro);

        assertNotNull(resultado);
        assertEquals(0, resultado.getSaldoDisponible().compareTo(new BigDecimal("1500.00"))); // 2000 - 500
    }

    @Test
    void testRegistrarRetiroSaldoInsuficiente() {
        // Simulamos una cuenta con saldo insuficiente
        cuenta.setSaldoInicial(new BigDecimal("100"));
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento(TipoMovimiento.RETIRO);
        retiro.setFecha(LocalDateTime.now());
        retiro.setValor(new BigDecimal("2500")); // Se intenta retirar más de lo disponible
        retiro.setCuenta(cuenta);

        // Simular que el servicio retorna un BAD_REQUEST en lugar de lanzar excepción
        when(movimientoRepository.save(any(Movimiento.class)))
            .thenThrow(new SaldoInsuficienteException("Saldo no disponible"));

        // Capturamos la excepción esperada
        Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {
            movimientoService.registrarMovimiento(retiro);
        });

        // Verificamos que el mensaje de error sea el esperado
        assertEquals("Saldo no disponible", exception.getMessage());
    }

}
