package com.barberia.barberos.controller;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.service.BarberoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @GetMapping
    public List<BarberoResponseDTO> listarBarberos() {
        return barberoService.listarBarberos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberoResponseDTO> buscarPorId(@PathVariable Long id) {
        return barberoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<BarberoResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return barberoService.buscarPorUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponible/{disponible}")
    public List<BarberoResponseDTO> buscarPorDisponible(@PathVariable Boolean disponible) {
        return barberoService.buscarPorDisponible(disponible);
    }

    @GetMapping("/especialidad/{especialidad}")
    public List<BarberoResponseDTO> buscarPorEspecialidad(@PathVariable String especialidad) {
        return barberoService.buscarPorEspecialidad(especialidad);
    }

    @PostMapping
    public BarberoResponseDTO guardarBarbero(@Valid @RequestBody BarberoRequestDTO dto) {
        return barberoService.guardarBarbero(dto);
    }

    @PutMapping("/{id}")
    public BarberoResponseDTO modificarBarbero(
            @PathVariable Long id,
            @Valid @RequestBody BarberoRequestDTO dto
    ) {
        return barberoService.modificarBarbero(id, dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarBarbero(@PathVariable Long id) {
        barberoService.eliminarBarbero(id);
        return "Barbero eliminado correctamente";
    }
}
