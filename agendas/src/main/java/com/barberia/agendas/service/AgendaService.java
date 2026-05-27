package com.barberia.agendas.service;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;
import com.barberia.agendas.dto.BarberoDTO;
import com.barberia.agendas.dto.UsuarioDTO;
import com.barberia.agendas.model.Agenda;
import com.barberia.agendas.repository.AgendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final RestTemplate restTemplate;

    public AgendaService(AgendaRepository agendaRepository, RestTemplate restTemplate) {
        this.agendaRepository = agendaRepository;
        this.restTemplate = restTemplate;
    }

    public List<AgendaResponseDTO> listarAgendas() {
        return agendaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<AgendaResponseDTO> buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public List<AgendaResponseDTO> buscarPorClienteId(Long clienteId) {
        return agendaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorBarberoId(Long barberoId) {
        return agendaRepository.findByBarberoId(barberoId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorEstado(String estado) {
        return agendaRepository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public AgendaResponseDTO guardarAgenda(AgendaRequestDTO dto) {

        boolean ocupado = agendaRepository.existsByBarberoIdAndFechaAndHoraAndEstadoNot(
                dto.getBarberoId(),
                dto.getFecha(),
                dto.getHora(),
                "CANCELADA"
        );

        if (ocupado) {
            throw new RuntimeException("El barbero ya tiene una reserva en esa fecha y hora");
        }

        Agenda agenda = new Agenda();

        agenda.setClienteId(dto.getClienteId());
        agenda.setBarberoId(dto.getBarberoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHora(dto.getHora());

        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            agenda.setEstado("RESERVADA");
        } else {
            agenda.setEstado(dto.getEstado());
        }

        Agenda agendaGuardada = agendaRepository.save(agenda);

        return convertirAResponseDTO(agendaGuardada);
    }

    public AgendaResponseDTO cancelarAgenda(Long id) {

        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

        agenda.setEstado("CANCELADA");

        Agenda agendaActualizada = agendaRepository.save(agenda);

        return convertirAResponseDTO(agendaActualizada);
    }

    public AgendaResponseDTO reprogramarAgenda(Long id, AgendaRequestDTO dto) {

        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

        boolean ocupado = agendaRepository.existsByBarberoIdAndFechaAndHoraAndEstadoNotAndIdNot(
                dto.getBarberoId(),
                dto.getFecha(),
                dto.getHora(),
                "CANCELADA",
                id
        );

        if (ocupado) {
            throw new RuntimeException("El barbero ya tiene una reserva en esa fecha y hora");
        }

        agenda.setClienteId(dto.getClienteId());
        agenda.setBarberoId(dto.getBarberoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHora(dto.getHora());
        agenda.setEstado("REPROGRAMADA");

        Agenda agendaActualizada = agendaRepository.save(agenda);

        return convertirAResponseDTO(agendaActualizada);
    }

    public void eliminarAgenda(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("Agenda no encontrada");
        }

        agendaRepository.deleteById(id);
    }

    private AgendaResponseDTO convertirAResponseDTO(Agenda agenda) {

        AgendaResponseDTO dto = new AgendaResponseDTO();

        dto.setId(agenda.getId());
        dto.setClienteId(agenda.getClienteId());
        dto.setBarberoId(agenda.getBarberoId());
        dto.setFecha(agenda.getFecha());
        dto.setHora(agenda.getHora());
        dto.setEstado(agenda.getEstado());

        try {
            UsuarioDTO cliente = restTemplate.getForObject(
                    "http://localhost:8081/api/usuarios/" + agenda.getClienteId(),
                    UsuarioDTO.class
            );

            if (cliente != null) {
                dto.setNombreCliente(cliente.getNombre());
                dto.setEmailCliente(cliente.getEmail());
                dto.setTelefonoCliente(cliente.getTelefono());
            }

        } catch (Exception e) {
            dto.setNombreCliente("Cliente no encontrado");
            dto.setEmailCliente("Sin información");
            dto.setTelefonoCliente("Sin información");
        }

        try {
            BarberoDTO barbero = restTemplate.getForObject(
                    "http://localhost:8082/api/barberos/" + agenda.getBarberoId(),
                    BarberoDTO.class
            );

            if (barbero != null) {
                dto.setNombreBarbero(barbero.getNombre());
                dto.setEspecialidadBarbero(barbero.getEspecialidad());
            }

        } catch (Exception e) {
            dto.setNombreBarbero("Barbero no encontrado");
            dto.setEspecialidadBarbero("Sin información");
        }

        return dto;
    }
}
