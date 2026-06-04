package com.barberia.resenas.controller;

import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;
import com.barberia.resenas.service.ResenaService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> listarResenas() {
        return ResponseEntity.ok(resenaService.listarResenas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(resenaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ResenaResponseDTO>> buscarPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(resenaService.buscarPorClienteId(clienteId));
    }

    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<ResenaResponseDTO>> buscarPorBarberoId(@PathVariable Long barberoId) {
        return ResponseEntity.ok(resenaService.buscarPorBarberoId(barberoId));
    }

    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<ResenaResponseDTO>> buscarPorCalificacion(@PathVariable Integer calificacion) {
        return ResponseEntity.ok(resenaService.buscarPorCalificacion(calificacion));
    }

    @PostMapping
    public ResponseEntity<ResenaResponseDTO> guardarResena(@Valid @RequestBody ResenaRequestDTO dto) {
        ResenaResponseDTO resenaCreada = resenaService.guardarResena(dto);
        URI location = URI.create("/api/resenas/" + resenaCreada.getId());

        return ResponseEntity.created(location).body(resenaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> modificarResena(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequestDTO dto
    ) {
        return ResponseEntity.ok(resenaService.modificarResena(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}
