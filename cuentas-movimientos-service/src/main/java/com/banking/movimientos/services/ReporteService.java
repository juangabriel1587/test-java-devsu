package com.banking.movimientos.services;

import com.banking.movimientos.dto.MovimientoDTO;
import com.banking.movimientos.exceptions.ClienteNoEncontradoException;
import com.banking.movimientos.exceptions.CuentaNoEncontradaException;
import com.banking.movimientos.models.Cuenta;
import com.banking.movimientos.models.Movimiento;
import com.banking.movimientos.models.TipoMovimiento;
import com.banking.movimientos.repositories.CuentaRepository;
import com.banking.movimientos.repositories.MovimientoRepository;
import com.banking.movimientos.services.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService implements IReporteService {

	  private final CuentaRepository cuentaRepository;
	    private final MovimientoRepository movimientoRepository;
	    private final RestTemplate restTemplate;
	    //private static final String CLIENTE_SERVICE_URL = "http://localhost:8081/clientes/{clienteId}";
	    private static final String CLIENTE_SERVICE_URL = "http://cliente-persona-service:8081/clientes/{clienteId}";

	    @Autowired
	    public ReporteService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository, RestTemplate restTemplate) {
	        this.cuentaRepository = cuentaRepository;
	        this.movimientoRepository = movimientoRepository;
	        this.restTemplate = restTemplate;
	    }

      
	    @Override
	    public List<MovimientoDTO> generarReporteEstadoCuenta(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
	        try {
	            
	            Map<String, Object> clienteData = obtenerDatosCliente(clienteId);
	            if (clienteData == null || !clienteData.containsKey("nombre")) {
	                throw new ClienteNoEncontradoException(clienteId);
	            }
	            
	            String nombreCliente = (String) clienteData.get("nombre");

	          
	            List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
	            if (cuentas.isEmpty()) {
	                throw new CuentaNoEncontradaException(clienteId);
	            }

	            return cuentas.stream().flatMap(cuenta -> {
	                // Obtener los movimientos en el rango de fechas
	                List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
	                        cuenta.getId(), fechaInicio.atStartOfDay(), fechaFin.plusDays(1).atStartOfDay()
	                );

	                // Calcular saldo actual
	                BigDecimal saldoActual = calcularSaldoActual(cuenta.getSaldoInicial(), movimientos);

	                // Transformar los movimientos en DTOs
	                return movimientos.stream().map(mov -> 
	                    new MovimientoDTO(
	                        mov.getFecha(),
	                        nombreCliente,
	                        cuenta.getNumeroCuenta(),
	                        cuenta.getTipoCuenta(),
	                        cuenta.getSaldoInicial(),
	                        cuenta.isEstado(),
	                        mov.getValor(),
	                        saldoActual
	                    )
	                );
	            }).collect(Collectors.toList());

	        } catch (ClienteNoEncontradoException | CuentaNoEncontradaException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException("Error inesperado al generar el reporte de estado de cuenta.", e);
	        }
	    }


	    
	    /**
	     * Llama al microservicio de clientes para obtener los datos del cliente.
	     */
	    private Map<String, Object> obtenerDatosCliente(Long clienteId) {
	        try {
	            return restTemplate.getForObject(CLIENTE_SERVICE_URL, Map.class, clienteId);
	        } catch (Exception e) {
	            return null;
	        }
	    }
	    
	    /**
	     * Calcula el saldo actual basado en los movimientos de la cuenta.
	     */
	    private BigDecimal calcularSaldoActual(BigDecimal saldoInicial, List<Movimiento> movimientos) {
	        BigDecimal saldoActual = saldoInicial;
	        for (Movimiento mov : movimientos) {
	            if (mov.getTipoMovimiento() == TipoMovimiento.DEPOSITO) {
	                saldoActual = saldoActual.add(mov.getValor());
	            } else if (mov.getTipoMovimiento() == TipoMovimiento.RETIRO) {
	                saldoActual = saldoActual.subtract(mov.getValor());
	            }
	        }
	        return saldoActual;
	    }

}
