package com.barberia.agendas.service;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;
import com.barberia.agendas.exception.ResourceNotFoundException;
import com.barberia.agendas.model.Agenda;
import com.barberia.agendas.repository.AgendaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @Mock private AgendaRepository agendaRepository;
    @InjectMocks private AgendaServiceImpl agendaService;
    private Agenda agenda;
    private AgendaRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        LocalDateTime fechaHora = LocalDateTime.of(2026, 7, 1, 10, 0);
        agenda = Agenda.builder().id(1L).clienteId(2L).barberoId(3L).servicioId(4L)
                .fechaHora(fechaHora).estado("RESERVADA").build();
        request = AgendaRequestDTO.builder().clienteId(2L).barberoId(3L).servicioId(4L)
                .fechaHora(fechaHora).estado("RESERVADA").build();
    }

    @Test
    void listarAgendas_deberiaRetornarLista() {
        when(agendaRepository.findAll()).thenReturn(List.of(agenda));

        List<AgendaResponseDTO> resultado = agendaService.listarAgendas();

        assertEquals(1, resultado.size());
        verify(agendaRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarAgenda() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        AgendaResponseDTO resultado = agendaService.buscarPorId(1L);

        assertEquals("RESERVADA", resultado.getEstado());
        verify(agendaRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agendaService.buscarPorId(99L));
    }

    @Test
    void guardarAgenda_deberiaGuardarAgenda() {
        when(agendaRepository.existsByBarberoIdAndFechaHoraAndEstadoNot(3L, request.getFechaHora(), "CANCELADA"))
                .thenReturn(false);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        AgendaResponseDTO resultado = agendaService.guardarAgenda(request);

        assertEquals(1L, resultado.getId());
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void actualizarAgenda_cuandoExiste_deberiaActualizarAgenda() {
        AgendaRequestDTO actualizada = AgendaRequestDTO.builder().clienteId(2L).barberoId(3L).servicioId(4L)
                .fechaHora(request.getFechaHora().plusHours(1)).estado("REPROGRAMADA").build();
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.existsByBarberoIdAndFechaHoraAndEstadoNotAndIdNot(3L, actualizada.getFechaHora(), "CANCELADA", 1L))
                .thenReturn(false);
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AgendaResponseDTO resultado = agendaService.actualizarAgenda(1L, actualizada);

        assertEquals("REPROGRAMADA", resultado.getEstado());
        verify(agendaRepository).save(agenda);
    }

    @Test
    void eliminarAgenda_cuandoExiste_deberiaEliminarAgenda() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        agendaService.eliminarAgenda(1L);

        verify(agendaRepository).delete(agenda);
    }
}
