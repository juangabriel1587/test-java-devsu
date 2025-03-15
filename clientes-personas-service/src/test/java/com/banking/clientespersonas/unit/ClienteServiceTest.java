package com.banking.clientespersonas.unit;


import com.banking.clientespersonas.exceptions.RecursoNoEncontradoException;
import com.banking.clientespersonas.models.Cliente;
import com.banking.clientespersonas.repositories.ClienteRepository;
import com.banking.clientespersonas.services.ClienteService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void obtenerClientePorId_ClienteExistente() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("José Lema");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.obtenerClientePorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("José Lema", resultado.getNombre());
    }

    @Test
    void obtenerClientePorId_ClienteNoEncontrado() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            clienteService.obtenerClientePorId(99L); // Ahora el método lanza la excepción directamente
        });

        assertEquals("Cliente no encontrado con ID: 99", exception.getMessage());
    }
}
