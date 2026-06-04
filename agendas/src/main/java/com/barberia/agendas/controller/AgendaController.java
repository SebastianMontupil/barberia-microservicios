package com.barberia.agendas.controller;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;
import com.barberia.agendas.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> listarAgendas() {
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendaResponseDTO>> buscarPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendaService.buscarPorClienteId(clienteId));
    }

    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<AgendaResponseDTO>> buscarPorBarberoId(@PathVariable Long barberoId) {
        return ResponseEntity.ok(agendaService.buscarPorBarberoId(barberoId));
    }

    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<AgendaResponseDTO>> buscarPorServicioId(@PathVariable Long servicioId) {
        return ResponseEntity.ok(agendaService.buscarPorServicioId(servicioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AgendaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(agendaService.buscarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> guardarAgenda(@Valid @RequestBody AgendaRequestDTO dto) {
        AgendaResponseDTO agendaCreada = agendaService.guardarAgenda(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agendaCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(agendaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> actualizarAgenda(
            @PathVariable Long id,
            @Valid @RequestBody AgendaRequestDTO dto
    ) {
        return ResponseEntity.ok(agendaService.actualizarAgenda(id, dto));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<AgendaResponseDTO> cancelarAgenda(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.cancelarAgenda(id));
    }

    @PutMapping("/reprogramar/{id}")
    public ResponseEntity<AgendaResponseDTO> reprogramarAgenda(
            @PathVariable Long id,
            @Valid @RequestBody AgendaRequestDTO dto
    ) {
        return ResponseEntity.ok(agendaService.reprogramarAgenda(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable Long id) {
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}
