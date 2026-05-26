package com.barberia.barberos.controller;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.service.BarberoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Optional<BarberoResponseDTO> buscarPorId(@PathVariable Long id) {
        return barberoService.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public Optional<BarberoResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return barberoService.buscarPorUsuarioId(usuarioId);
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
    public BarberoResponseDTO guardarBarbero(@RequestBody BarberoRequestDTO dto) {
        return barberoService.guardarBarbero(dto);
    }

    @PutMapping("/{id}")
    public BarberoResponseDTO modificarBarbero(
            @PathVariable Long id,
            @RequestBody BarberoRequestDTO dto
    ) {
        return barberoService.modificarBarbero(id, dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarBarbero(@PathVariable Long id) {
        barberoService.eliminarBarbero(id);
        return "Barbero eliminado correctamente";
    }
}