package com.barberia.reportes.service;

import com.barberia.reportes.dto.AgendaDTO;
import com.barberia.reportes.dto.PagoDTO;
import com.barberia.reportes.dto.ReporteDTO;
import com.barberia.reportes.dto.ReporteRequestDTO;
import com.barberia.reportes.dto.ResenaDTO;
import com.barberia.reportes.exception.ReporteProcessingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ReporteServiceImpl implements ReporteService {

    private static final String PAGOS_URL = "http://localhost:8085/api/pagos";
    private static final String AGENDAS_URL = "http://localhost:8083/api/agendas";
    private static final String RESENAS_URL = "http://localhost:8087/api/resenas";

    private final RestTemplate restTemplate;

    public ReporteServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ReporteDTO generarReporteConsolidado(ReporteRequestDTO reporteRequestDTO) {
        log.info("Inicio de generacion de reporte consolidado. fechaInicio={}, fechaFin={}",
                reporteRequestDTO.getFechaInicio(), reporteRequestDTO.getFechaFin());

        try {
            LocalDate fechaInicio = reporteRequestDTO.getFechaInicio().toLocalDate();
            LocalDate fechaFin = reporteRequestDTO.getFechaFin().toLocalDate();

            List<PagoDTO> pagos = obtenerListaPagos(PAGOS_URL).stream()
                    .filter(pago -> fechaEnRango(pago.getFechaPago(), fechaInicio, fechaFin))
                    .toList();

            List<AgendaDTO> agendas = obtenerListaAgendas(AGENDAS_URL).stream()
                    .filter(agenda -> fechaEnRango(agenda.getFecha(), fechaInicio, fechaFin))
                    .toList();

            List<ResenaDTO> resenas = obtenerListaResenas(RESENAS_URL).stream()
                    .filter(resena -> fechaEnRango(resena.getFechaResena(), fechaInicio, fechaFin))
                    .toList();

            Integer totalIngresos = pagos.stream()
                    .filter(pago -> "PAGADO".equalsIgnoreCase(pago.getEstadoPago()))
                    .map(PagoDTO::getMonto)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();

            Double promedioCalificacion = resenas.stream()
                    .map(ResenaDTO::getCalificacion)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte consolidado")
                    .descripcion("Reporte consolidado de ingresos, citas y desempeno entre "
                            + fechaInicio + " y " + fechaFin)
                    .totalRegistros(pagos.size() + agendas.size() + resenas.size())
                    .totalIngresos(totalIngresos)
                    .promedioCalificacion(promedioCalificacion)
                    .build();

            log.info("Fin de generacion de reporte consolidado. totalRegistros={}, totalIngresos={}, promedioCalificacion={}",
                    reporteDTO.getTotalRegistros(), reporteDTO.getTotalIngresos(), reporteDTO.getPromedioCalificacion());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte consolidado", e);
            throw new ReporteProcessingException("Error al procesar el reporte consolidado", e);
        }
    }

    @Override
    public ReporteDTO reporteIngresos() {
        log.info("Inicio de generacion de reporte de ingresos");

        try {
            List<PagoDTO> pagos = obtenerListaPagos(PAGOS_URL);

            int totalIngresos = pagos.stream()
                    .filter(pago -> "PAGADO".equalsIgnoreCase(pago.getEstadoPago()))
                    .map(PagoDTO::getMonto)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte de ingresos")
                    .descripcion("Total de ingresos generados por pagos realizados")
                    .totalRegistros(pagos.size())
                    .totalIngresos(totalIngresos)
                    .promedioCalificacion(null)
                    .build();

            log.info("Fin de generacion de reporte de ingresos. totalRegistros={}, totalIngresos={}",
                    reporteDTO.getTotalRegistros(), reporteDTO.getTotalIngresos());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte de ingresos", e);
            throw new ReporteProcessingException("Error al procesar el reporte de ingresos", e);
        }
    }

    @Override
    public ReporteDTO reporteCitas() {
        log.info("Inicio de generacion de reporte de citas");

        try {
            List<AgendaDTO> agendas = obtenerListaAgendas(AGENDAS_URL);

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte de citas")
                    .descripcion("Cantidad total de citas registradas en el sistema")
                    .totalRegistros(agendas.size())
                    .totalIngresos(null)
                    .promedioCalificacion(null)
                    .build();

            log.info("Fin de generacion de reporte de citas. totalRegistros={}", reporteDTO.getTotalRegistros());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte de citas", e);
            throw new ReporteProcessingException("Error al procesar el reporte de citas", e);
        }
    }

    @Override
    public ReporteDTO reporteCitasPorBarbero(Long barberoId) {
        log.info("Inicio de generacion de reporte de citas por barbero. barberoId={}", barberoId);

        try {
            List<AgendaDTO> agendas = obtenerListaAgendas(AGENDAS_URL + "/barbero/" + barberoId);

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte de citas por barbero")
                    .descripcion("Cantidad de citas asociadas al barbero con ID " + barberoId)
                    .totalRegistros(agendas.size())
                    .totalIngresos(null)
                    .promedioCalificacion(null)
                    .build();

            log.info("Fin de generacion de reporte de citas por barbero. barberoId={}, totalRegistros={}",
                    barberoId, reporteDTO.getTotalRegistros());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte de citas por barbero. barberoId={}", barberoId, e);
            throw new ReporteProcessingException("Error al procesar el reporte de citas por barbero", e);
        }
    }

    @Override
    public ReporteDTO reporteCalificacionBarbero(Long barberoId) {
        log.info("Inicio de generacion de reporte de calificacion de barbero. barberoId={}", barberoId);

        try {
            List<ResenaDTO> resenas = obtenerListaResenas(RESENAS_URL + "/barbero/" + barberoId);

            double promedio = resenas.stream()
                    .map(ResenaDTO::getCalificacion)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte de calificacion de barbero")
                    .descripcion("Promedio de calificaciones del barbero con ID " + barberoId)
                    .totalRegistros(resenas.size())
                    .totalIngresos(null)
                    .promedioCalificacion(promedio)
                    .build();

            log.info("Fin de generacion de reporte de calificacion de barbero. barberoId={}, promedioCalificacion={}",
                    barberoId, reporteDTO.getPromedioCalificacion());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte de calificacion de barbero. barberoId={}", barberoId, e);
            throw new ReporteProcessingException("Error al procesar el reporte de calificacion de barbero", e);
        }
    }

    @Override
    public ReporteDTO reporteDesempenoBarbero(Long barberoId) {
        log.info("Inicio de generacion de reporte de desempeno de barbero. barberoId={}", barberoId);

        try {
            List<AgendaDTO> agendas = obtenerListaAgendas(AGENDAS_URL + "/barbero/" + barberoId);
            List<ResenaDTO> resenas = obtenerListaResenas(RESENAS_URL + "/barbero/" + barberoId);

            double promedio = resenas.stream()
                    .map(ResenaDTO::getCalificacion)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);

            ReporteDTO reporteDTO = ReporteDTO.builder()
                    .titulo("Reporte de desempeno de barbero")
                    .descripcion("Muestra cantidad de citas y promedio de calificaciones del barbero con ID " + barberoId)
                    .totalRegistros(agendas.size())
                    .totalIngresos(null)
                    .promedioCalificacion(promedio)
                    .build();

            log.info("Fin de generacion de reporte de desempeno de barbero. barberoId={}, totalRegistros={}, promedioCalificacion={}",
                    barberoId, reporteDTO.getTotalRegistros(), reporteDTO.getPromedioCalificacion());
            return reporteDTO;
        } catch (Exception e) {
            log.error("Error al procesar el reporte de desempeno de barbero. barberoId={}", barberoId, e);
            throw new ReporteProcessingException("Error al procesar el reporte de desempeno de barbero", e);
        }
    }

    private List<PagoDTO> obtenerListaPagos(String url) {
        PagoDTO[] respuesta = restTemplate.getForObject(url, PagoDTO[].class);
        return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
    }

    private List<AgendaDTO> obtenerListaAgendas(String url) {
        AgendaDTO[] respuesta = restTemplate.getForObject(url, AgendaDTO[].class);
        return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
    }

    private List<ResenaDTO> obtenerListaResenas(String url) {
        ResenaDTO[] respuesta = restTemplate.getForObject(url, ResenaDTO[].class);
        return respuesta == null ? Collections.emptyList() : Arrays.asList(respuesta);
    }

    private boolean fechaEnRango(LocalDate fecha, LocalDate fechaInicio, LocalDate fechaFin) {
        return fecha != null && !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
}
