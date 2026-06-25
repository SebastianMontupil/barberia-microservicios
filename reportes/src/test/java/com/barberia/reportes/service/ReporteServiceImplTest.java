package com.barberia.reportes.service;

import com.barberia.reportes.dto.AgendaDTO;
import com.barberia.reportes.dto.PagoDTO;
import com.barberia.reportes.dto.ReporteDTO;
import com.barberia.reportes.dto.ReporteRequestDTO;
import com.barberia.reportes.dto.ResenaDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceImplTest {

    private static final String PAGOS_URL = "http://pagos/api/pagos";
    private static final String AGENDAS_URL = "http://agendas/api/agendas";
    private static final String RESENAS_URL = "http://resenas/api/resenas";

    @Mock private RestTemplate restTemplate;
    @InjectMocks private ReporteServiceImpl reporteService;

    @Test
    void reporteIngresos_deberiaSumarSoloPagosPagados() {
        PagoDTO pagado = new PagoDTO(1L, 2L, 3L, 12000, "EFECTIVO", "PAGADO", LocalDate.of(2026, 6, 20));
        PagoDTO pendiente = new PagoDTO(2L, 2L, 4L, 8000, "TARJETA", "PENDIENTE", LocalDate.of(2026, 6, 20));
        when(restTemplate.getForObject(eq(PAGOS_URL), eq(PagoDTO[].class)))
                .thenReturn(new PagoDTO[]{pagado, pendiente});

        ReporteDTO resultado = reporteService.reporteIngresos();

        assertEquals(2, resultado.getTotalRegistros());
        assertEquals(12000, resultado.getTotalIngresos());
        verify(restTemplate).getForObject(PAGOS_URL, PagoDTO[].class);
    }

    @Test
    void generarReporteConsolidado_deberiaFiltrarPorRangoDeFechas() {
        LocalDate fecha = LocalDate.of(2026, 6, 20);
        PagoDTO pago = new PagoDTO(1L, 2L, 3L, 10000, "EFECTIVO", "PAGADO", fecha);
        AgendaDTO agenda = new AgendaDTO(1L, 2L, "Ana", 3L, "Carlos", fecha, null, "RESERVADA");
        ResenaDTO resena = new ResenaDTO(1L, 2L, "Ana", 3L, "Carlos", 4, "Muy bien", fecha);
        when(restTemplate.getForObject(eq(PAGOS_URL), eq(PagoDTO[].class))).thenReturn(new PagoDTO[]{pago});
        when(restTemplate.getForObject(eq(AGENDAS_URL), eq(AgendaDTO[].class))).thenReturn(new AgendaDTO[]{agenda});
        when(restTemplate.getForObject(eq(RESENAS_URL), eq(ResenaDTO[].class))).thenReturn(new ResenaDTO[]{resena});
        ReporteRequestDTO request = ReporteRequestDTO.builder()
                .fechaInicio(LocalDateTime.of(2026, 6, 1, 0, 0))
                .fechaFin(LocalDateTime.of(2026, 6, 30, 23, 59))
                .build();

        ReporteDTO resultado = reporteService.generarReporteConsolidado(request);

        assertEquals(3, resultado.getTotalRegistros());
        assertEquals(10000, resultado.getTotalIngresos());
        assertEquals(4.0, resultado.getPromedioCalificacion());
    }

    @Test
    void reporteCitas_cuandoServicioExternoFalla_deberiaLanzarExcepcion() {
        when(restTemplate.getForObject(eq(AGENDAS_URL), eq(AgendaDTO[].class)))
                .thenThrow(new RuntimeException("Servicio no disponible"));

        assertThrows(RuntimeException.class, () -> reporteService.reporteCitas());
    }
}
