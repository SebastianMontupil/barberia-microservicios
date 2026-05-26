package com.barberia.reportes.service;

import com.barberia.reportes.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ReporteService {

    private final RestTemplate restTemplate;

    public ReporteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ReporteDTO reporteIngresos() {

        PagoDTO[] pagosArray = restTemplate.getForObject(
                "http://localhost:8085/api/pagos",
                PagoDTO[].class
        );

        List<PagoDTO> pagos = Arrays.asList(pagosArray);

        int totalIngresos = pagos.stream()
                .filter(pago -> "PAGADO".equalsIgnoreCase(pago.getEstadoPago()))
                .mapToInt(PagoDTO::getMonto)
                .sum();

        return new ReporteDTO(
                "Reporte de ingresos",
                "Total de ingresos generados por pagos realizados",
                pagos.size(),
                totalIngresos,
                null
        );
    }

    public ReporteDTO reporteCitas() {

        AgendaDTO[] agendasArray = restTemplate.getForObject(
                "http://localhost:8083/api/agendas",
                AgendaDTO[].class
        );

        List<AgendaDTO> agendas = Arrays.asList(agendasArray);

        return new ReporteDTO(
                "Reporte de citas",
                "Cantidad total de citas registradas en el sistema",
                agendas.size(),
                null,
                null
        );
    }

    public ReporteDTO reporteCitasPorBarbero(Long barberoId) {

        AgendaDTO[] agendasArray = restTemplate.getForObject(
                "http://localhost:8083/api/agendas/barbero/" + barberoId,
                AgendaDTO[].class
        );

        List<AgendaDTO> agendas = Arrays.asList(agendasArray);

        return new ReporteDTO(
                "Reporte de citas por barbero",
                "Cantidad de citas asociadas al barbero con ID " + barberoId,
                agendas.size(),
                null,
                null
        );
    }

    public ReporteDTO reporteCalificacionBarbero(Long barberoId) {

        ResenaDTO[] resenasArray = restTemplate.getForObject(
                "http://localhost:8087/api/resenas/barbero/" + barberoId,
                ResenaDTO[].class
        );

        List<ResenaDTO> resenas = Arrays.asList(resenasArray);

        double promedio = resenas.stream()
                .mapToInt(ResenaDTO::getCalificacion)
                .average()
                .orElse(0.0);

        return new ReporteDTO(
                "Reporte de calificación de barbero",
                "Promedio de calificaciones del barbero con ID " + barberoId,
                resenas.size(),
                null,
                promedio
        );
    }

    public ReporteDTO reporteDesempenoBarbero(Long barberoId) {

        AgendaDTO[] agendasArray = restTemplate.getForObject(
                "http://localhost:8083/api/agendas/barbero/" + barberoId,
                AgendaDTO[].class
        );

        ResenaDTO[] resenasArray = restTemplate.getForObject(
                "http://localhost:8087/api/resenas/barbero/" + barberoId,
                ResenaDTO[].class
        );

        List<AgendaDTO> agendas = Arrays.asList(agendasArray);
        List<ResenaDTO> resenas = Arrays.asList(resenasArray);

        double promedio = resenas.stream()
                .mapToInt(ResenaDTO::getCalificacion)
                .average()
                .orElse(0.0);

        return new ReporteDTO(
                "Reporte de desempeño de barbero",
                "Muestra cantidad de citas y promedio de calificaciones del barbero con ID " + barberoId,
                agendas.size(),
                null,
                promedio
        );
    }
}