package com.barberia.reportes.controller;

import com.barberia.reportes.dto.ReporteDTO;
import com.barberia.reportes.dto.ReporteRequestDTO;
import com.barberia.reportes.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("/consolidado")
    public ResponseEntity<ReporteDTO> generarReporteConsolidado(
            @Valid @RequestBody ReporteRequestDTO reporteRequestDTO) {
        return ResponseEntity.ok(reporteService.generarReporteConsolidado(reporteRequestDTO));
    }

    @GetMapping("/ingresos")
    public ResponseEntity<ReporteDTO> reporteIngresos() {
        return ResponseEntity.ok(reporteService.reporteIngresos());
    }

    @GetMapping("/citas")
    public ResponseEntity<ReporteDTO> reporteCitas() {
        return ResponseEntity.ok(reporteService.reporteCitas());
    }

    @GetMapping("/citas/barbero/{barberoId}")
    public ResponseEntity<ReporteDTO> reporteCitasPorBarbero(@PathVariable Long barberoId) {
        return ResponseEntity.ok(reporteService.reporteCitasPorBarbero(barberoId));
    }

    @GetMapping("/calificacion/barbero/{barberoId}")
    public ResponseEntity<ReporteDTO> reporteCalificacionBarbero(@PathVariable Long barberoId) {
        return ResponseEntity.ok(reporteService.reporteCalificacionBarbero(barberoId));
    }

    @GetMapping("/desempeno/barbero/{barberoId}")
    public ResponseEntity<ReporteDTO> reporteDesempenoBarbero(@PathVariable Long barberoId) {
        return ResponseEntity.ok(reporteService.reporteDesempenoBarbero(barberoId));
    }
}
