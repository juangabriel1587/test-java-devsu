package com.banking.movimientos.controllers;

import com.banking.movimientos.dto.MovimientoDTO;
import com.banking.movimientos.services.IReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final IReporteService reporteService;

    public ReporteController(IReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Genera un reporte del estado de cuenta basado en un rango de fechas y clienteId.
     *
     * @param clienteId   ID del cliente.
     * @param fechaInicio Fecha inicial en formato YYYY-MM-DD.
     * @param fechaFin    Fecha final en formato YYYY-MM-DD.
     * @return Lista de reportes con la estructura esperada en formato Map<String, Object>.
     */
    @GetMapping
    public ResponseEntity<?> obtenerReporteEstadoCuenta(
            @RequestParam Long clienteId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
   
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);

            if (inicio.isAfter(fin)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "La fecha de inicio no puede ser posterior a la fecha de fin.",
                        "codigo", HttpStatus.BAD_REQUEST.value()
                ));
            }

           
            List<MovimientoDTO> reporte = reporteService.generarReporteEstadoCuenta(clienteId, inicio, fin);

           
            if (reporte.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "mensaje", "No se encontraron movimientos para el cliente en el rango de fechas especificado.",
                        "codigo", HttpStatus.OK.value()
                ));
            }

            return ResponseEntity.ok(reporte);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Ocurrió un error al generar el reporte.",
                    "detalle", ex.getMessage(),
                    "codigo", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }

    /**
     * Manejo global de excepciones de argumentos inválidos.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage(),
                "codigo", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * Manejo global de excepciones inesperadas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "detalle", ex.getMessage(),
                "codigo", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}
