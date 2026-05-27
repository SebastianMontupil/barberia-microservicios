package com.barberia.reportes.service;

import com.barberia.reportes.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ReporteService {

    private final RestTemplate restTemplate;

    public ReporteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ReporteDTO reporteIngresos() {

        List<PagoDTO> pagos = obtenerListaPagos(
                "http://localhost:8085/api/pagos"
        );

        int totalIngresos = pagos.stream()
                .filter(pago -> "PAGADO".equalsIgnoreCase(pago.getEstadoPago()))
                .map(PagoDTO::getMonto)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
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

        List<AgendaDTO> agendas = obtenerListaAgendas(
                "http://localhost:8083/api/agendas"
        );

        return new ReporteDTO(
                "Reporte de citas",
                "Cantidad total de citas registradas en el sistema",
                agendas.size(),
                null,
                null
        );
    }

    public ReporteDTO reporteCitasPorBarbero(Long barberoId) {

        List<AgendaDTO> agendas = obtenerListaAgendas(
                "http://localhost:8083/api/agendas/barbero/" + barberoId
        );

        return new ReporteDTO(
                "Reporte de citas por barbero",
                "Cantidad de citas asociadas al barbero con ID " + barberoId,
                agendas.size(),
                null,
                null
        );
    }

    public ReporteDTO reporteCalificacionBarbero(Long barberoId) {

        List<ResenaDTO> resenas = obtenerListaResenas(
                "http://localhost:8087/api/resenas/barbero/" + barberoId
        );

        double promedio = resenas.stream()
                .map(ResenaDTO::getCalificacion)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
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

        List<AgendaDTO> agendas = obtenerListaAgendas(
                "http://localhost:8083/api/agendas/barbero/" + barberoId
        );

        List<ResenaDTO> resenas = obtenerListaResenas(
                "http://localhost:8087/api/resenas/barbero/" + barberoId
        );

        double promedio = resenas.stream()
                .map(ResenaDTO::getCalificacion)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
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

    private List<PagoDTO> obtenerListaPagos(String url) {
        try {
            PagoDTO[] respuesta = restTemplate.getForObject(url, PagoDTO[].class);
            return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<AgendaDTO> obtenerListaAgendas(String url) {
        try {
            AgendaDTO[] respuesta = restTemplate.getForObject(url, AgendaDTO[].class);
            return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<ResenaDTO> obtenerListaResenas(String url) {
        try {
            ResenaDTO[] respuesta = restTemplate.getForObject(url, ResenaDTO[].class);
            return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
