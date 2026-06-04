package com.barberia.inventario.controller;

import com.barberia.inventario.dto.ProductoRequestDTO;
import com.barberia.inventario.dto.ProductoResponseDTO;
import com.barberia.inventario.service.ProductoService;
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
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/disponible/{disponible}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorDisponible(@PathVariable Boolean disponible) {
        return ResponseEntity.ok(productoService.buscarPorDisponible(disponible));
    }

    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.verificarDisponibilidad(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoResponseDTO>> productosConBajoStock() {
        return ResponseEntity.ok(productoService.productosConBajoStock());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO productoCreado = productoService.guardarProducto(dto);
        URI location = URI.create("/api/productos/" + productoCreado.getId());

        return ResponseEntity.created(location).body(productoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> modificarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity.ok(productoService.modificarProducto(id, dto));
    }

    @PutMapping("/aumentar/{id}/{cantidad}")
    public ResponseEntity<ProductoResponseDTO> aumentarStock(
            @PathVariable Long id,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(productoService.aumentarStock(id, cantidad));
    }

    @PutMapping("/disminuir/{id}/{cantidad}")
    public ResponseEntity<ProductoResponseDTO> disminuirStock(
            @PathVariable Long id,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(productoService.disminuirStock(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
