package com.barberia.reportes.service;

import com.barberia.reportes.dto.ReporteDTO;
import com.barberia.reportes.dto.ReporteRequestDTO;

public interface ReporteService {

    ReporteDTO generarReporteConsolidado(ReporteRequestDTO reporteRequestDTO);

    ReporteDTO reporteIngresos();

    ReporteDTO reporteCitas();

    ReporteDTO reporteCitasPorBarbero(Long barberoId);

    ReporteDTO reporteCalificacionBarbero(Long barberoId);

    ReporteDTO reporteDesempenoBarbero(Long barberoId);
}
