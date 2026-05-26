package com.barberia.reportes.controller;

import com.barberia.reportes.dto.ReporteDTO;
import com.barberia.reportes.service.ReporteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ingresos")
    public ReporteDTO reporteIngresos() {
        return reporteService.reporteIngresos();
    }

    @GetMapping("/citas")
    public ReporteDTO reporteCitas() {
        return reporteService.reporteCitas();
    }

    @GetMapping("/citas/barbero/{barberoId}")
    public ReporteDTO reporteCitasPorBarbero(@PathVariable Long barberoId) {
        return reporteService.reporteCitasPorBarbero(barberoId);
    }

    @GetMapping("/calificacion/barbero/{barberoId}")
    public ReporteDTO reporteCalificacionBarbero(@PathVariable Long barberoId) {
        return reporteService.reporteCalificacionBarbero(barberoId);
    }

    @GetMapping("/desempeno/barbero/{barberoId}")
    public ReporteDTO reporteDesempenoBarbero(@PathVariable Long barberoId) {
        return reporteService.reporteDesempenoBarbero(barberoId);
    }
}