package com.barberia.agendas.controller;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;
import com.barberia.agendas.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public List<AgendaResponseDTO> listarAgendas() {
        return agendaService.listarAgendas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return agendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<AgendaResponseDTO> buscarPorClienteId(@PathVariable Long clienteId) {
        return agendaService.buscarPorClienteId(clienteId);
    }

    @GetMapping("/barbero/{barberoId}")
    public List<AgendaResponseDTO> buscarPorBarberoId(@PathVariable Long barberoId) {
        return agendaService.buscarPorBarberoId(barberoId);
    }

    @GetMapping("/estado/{estado}")
    public List<AgendaResponseDTO> buscarPorEstado(@PathVariable String estado) {
        return agendaService.buscarPorEstado(estado);
    }

    @PostMapping
    public AgendaResponseDTO guardarAgenda(@Valid @RequestBody AgendaRequestDTO dto) {
        return agendaService.guardarAgenda(dto);
    }

    @PutMapping("/cancelar/{id}")
    public AgendaResponseDTO cancelarAgenda(@PathVariable Long id) {
        return agendaService.cancelarAgenda(id);
    }

    @PutMapping("/reprogramar/{id}")
    public AgendaResponseDTO reprogramarAgenda(
            @PathVariable Long id,
            @Valid @RequestBody AgendaRequestDTO dto
    ) {
        return agendaService.reprogramarAgenda(id, dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarAgenda(@PathVariable Long id) {
        agendaService.eliminarAgenda(id);
        return "Agenda eliminada correctamente";
    }
}
