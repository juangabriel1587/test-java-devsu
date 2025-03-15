package com.banking.movimientos.unit;

import com.banking.movimientos.models.Cuenta;
import com.banking.movimientos.repositories.CuentaRepository;
import com.banking.movimientos.services.CuentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456");
        cuenta.setEstado(true);
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setTipoCuenta("Corriente");
    }

    @Test
    void testCrearCuenta() {
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(100L); // Simula la generación de ID por la BD
            return c;
        });

        Cuenta nuevaCuenta = cuentaService.crearCuenta(cuenta);
        assertNotNull(nuevaCuenta);
        assertNotNull(nuevaCuenta.getId()); // Asegura que tiene un ID asignado
        assertEquals("123456", nuevaCuenta.getNumeroCuenta());
    }

    @Test
    void testObtenerCuentaPorId() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Cuenta encontrada = cuentaService.obtenerCuentaPorId(100L);
        assertNotNull(encontrada);
        assertEquals(0, encontrada.getSaldoInicial().compareTo(new BigDecimal("2000.00"))); // Comparación segura con BigDecimal
    }

    @Test
    void testActualizarCuenta() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cuenta.setSaldoInicial(new BigDecimal("5000.00"));
        Cuenta actualizada = cuentaService.actualizarCuenta(100L, cuenta);
        
        assertNotNull(actualizada);
        assertEquals(0, actualizada.getSaldoInicial().compareTo(new BigDecimal("5000.00"))); // Comparación segura con BigDecimal
    }
}
