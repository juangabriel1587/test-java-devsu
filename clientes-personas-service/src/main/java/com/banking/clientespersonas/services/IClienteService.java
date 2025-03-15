package com.banking.clientespersonas.services;


import com.banking.clientespersonas.models.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    List<Cliente> obtenerTodosLosClientes();
    Cliente obtenerClientePorId(Long id);
    Optional<Cliente> obtenerClientePorIdentidad(String identidad);
    Cliente crearCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente clienteActualizado);
    void eliminarCliente(Long id);
}
