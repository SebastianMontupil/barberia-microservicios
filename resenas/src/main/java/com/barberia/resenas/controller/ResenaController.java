package com.barberia.resenas.controller;

import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;
import com.barberia.resenas.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public List<ResenaResponseDTO> listarResenas() {
        return resenaService.listarResenas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> buscarPorId(@PathVariable Long id) {
        return resenaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<ResenaResponseDTO> buscarPorClienteId(@PathVariable Long clienteId) {
        return resenaService.buscarPorClienteId(clienteId);
    }

    @GetMapping("/barbero/{barberoId}")
    public List<ResenaResponseDTO> buscarPorBarberoId(@PathVariable Long barberoId) {
        return resenaService.buscarPorBarberoId(barberoId);
    }

    @GetMapping("/calificacion/{calificacion}")
    public List<ResenaResponseDTO> buscarPorCalificacion(@PathVariable Integer calificacion) {
        return resenaService.buscarPorCalificacion(calificacion);
    }

    @PostMapping
    public ResenaResponseDTO guardarResena(@Valid @RequestBody ResenaRequestDTO dto) {
        return resenaService.guardarResena(dto);
    }

    @PutMapping("/{id}")
    public ResenaResponseDTO modificarResena(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequestDTO dto
    ) {
        return resenaService.modificarResena(id, dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarResena(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return "Reseña eliminada correctamente";
    }
}
