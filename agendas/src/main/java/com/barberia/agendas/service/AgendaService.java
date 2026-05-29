package com.barberia.agendas.service;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;

import java.util.List;

public interface AgendaService {

    List<AgendaResponseDTO> listarAgendas();

    AgendaResponseDTO buscarPorId(Long id);

    List<AgendaResponseDTO> buscarPorClienteId(Long clienteId);

    List<AgendaResponseDTO> buscarPorBarberoId(Long barberoId);

    List<AgendaResponseDTO> buscarPorServicioId(Long servicioId);

    List<AgendaResponseDTO> buscarPorEstado(String estado);

    AgendaResponseDTO guardarAgenda(AgendaRequestDTO dto);

    AgendaResponseDTO actualizarAgenda(Long id, AgendaRequestDTO dto);

    AgendaResponseDTO cancelarAgenda(Long id);

    AgendaResponseDTO reprogramarAgenda(Long id, AgendaRequestDTO dto);

    void eliminarAgenda(Long id);
}
