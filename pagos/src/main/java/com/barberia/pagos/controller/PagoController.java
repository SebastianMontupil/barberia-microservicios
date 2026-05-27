package com.barberia.pagos.controller;

import jakarta.validation.Valid;
import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;
import com.barberia.pagos.service.PagoService;
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
    public List<PagoResponseDTO> listarPagos() {
        return pagoService.listarPagos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscarPorId(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PagoResponseDTO> buscarPorClienteId(@PathVariable Long clienteId) {
        return pagoService.buscarPorClienteId(clienteId);
    }

    @GetMapping("/agenda/{agendaId}")
    public List<PagoResponseDTO> buscarPorAgendaId(@PathVariable Long agendaId) {
        return pagoService.buscarPorAgendaId(agendaId);
    }

    @GetMapping("/estado/{estadoPago}")
    public List<PagoResponseDTO> buscarPorEstadoPago(@PathVariable String estadoPago) {
        return pagoService.buscarPorEstadoPago(estadoPago);
    }

    @GetMapping("/metodo/{metodoPago}")
    public List<PagoResponseDTO> buscarPorMetodoPago(@PathVariable String metodoPago) {
        return pagoService.buscarPorMetodoPago(metodoPago);
    }

    @PostMapping
    public PagoResponseDTO guardarPago(@Valid @RequestBody PagoRequestDTO dto) {
        return pagoService.guardarPago(dto);
    }

    @PutMapping("/estado/{id}/{estadoPago}")
    public PagoResponseDTO modificarEstadoPago(
            @PathVariable Long id,
            @PathVariable String estadoPago
    ) {
        return pagoService.modificarEstadoPago(id, estadoPago);
    }

    @DeleteMapping("/{id}")
    public String eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return "Pago eliminado correctamente";
    }
}
