package com.barberia.servicios.controller;

import com.barberia.servicios.dto.ServicioRequestDTO;
import com.barberia.servicios.dto.ServicioResponseDTO;
import com.barberia.servicios.service.ServicioService;
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
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> listarServicios() {
        return ResponseEntity.ok(servicioService.listarServicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ServicioResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(servicioService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<ServicioResponseDTO> guardarServicio(@Valid @RequestBody ServicioRequestDTO dto) {
        ServicioResponseDTO servicioCreado = servicioService.guardarServicio(dto);
        URI location = URI.create("/api/servicios/" + servicioCreado.getId());

        return ResponseEntity.created(location).body(servicioCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> modificarServicio(
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO dto
    ) {
        return ResponseEntity.ok(servicioService.modificarServicio(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
