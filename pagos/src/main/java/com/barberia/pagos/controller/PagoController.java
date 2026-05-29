package com.barberia.pagos.controller;

import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;
import com.barberia.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pagoService.buscarPorClienteId(clienteId));
    }

    @GetMapping("/agenda/{agendaId}")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorAgendaId(@PathVariable Long agendaId) {
        return ResponseEntity.ok(pagoService.buscarPorAgendaId(agendaId));
    }

    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorEstadoPago(@PathVariable String estadoPago) {
        return ResponseEntity.ok(pagoService.buscarPorEstadoPago(estadoPago));
    }

    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorMetodoPago(@PathVariable String metodoPago) {
        return ResponseEntity.ok(pagoService.buscarPorMetodoPago(metodoPago));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> guardarPago(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardarPago(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO dto
    ) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, dto));
    }

    @PutMapping("/{id}/estado/{estadoPago}")
    public ResponseEntity<PagoResponseDTO> modificarEstadoPago(
            @PathVariable Long id,
            @PathVariable String estadoPago
    ) {
        return ResponseEntity.ok(pagoService.modificarEstadoPago(id, estadoPago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
