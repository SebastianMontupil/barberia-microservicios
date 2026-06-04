package com.barberia.barberos.controller;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.dto.BarberoDisponibilidadRequestDTO;
import com.barberia.barberos.service.BarberoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/barberos")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @GetMapping
    public ResponseEntity<List<BarberoResponseDTO>> listarBarberos() {
        return ResponseEntity.ok(barberoService.listarBarberos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(barberoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<BarberoResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(barberoService.buscarPorUsuarioId(usuarioId));
    }

    @GetMapping("/disponible/{disponible}")
    public ResponseEntity<List<BarberoResponseDTO>> buscarPorDisponible(@PathVariable Boolean disponible) {
        return ResponseEntity.ok(barberoService.buscarPorDisponible(disponible));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<BarberoResponseDTO>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(barberoService.buscarPorEspecialidad(especialidad));
    }

    @PostMapping
    public ResponseEntity<BarberoResponseDTO> guardarBarbero(@Valid @RequestBody BarberoRequestDTO dto) {
        BarberoResponseDTO barberoCreado = barberoService.guardarBarbero(dto);
        URI location = URI.create("/api/barberos/" + barberoCreado.getId());

        return ResponseEntity.created(location).body(barberoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberoResponseDTO> modificarBarbero(
            @PathVariable Long id,
            @Valid @RequestBody BarberoRequestDTO dto
    ) {
        return ResponseEntity.ok(barberoService.modificarBarbero(id, dto));
    }

    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<BarberoResponseDTO> actualizarDisponibilidad(
            @PathVariable Long id,
            @Valid @RequestBody BarberoDisponibilidadRequestDTO dto
    ) {
        return ResponseEntity.ok(barberoService.actualizarDisponibilidad(id, dto.getDisponible()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBarbero(@PathVariable Long id) {
        barberoService.eliminarBarbero(id);
        return ResponseEntity.noContent().build();
    }
}
