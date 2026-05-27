package com.barberia.inventario.controller;

import com.barberia.inventario.model.Producto;
import com.barberia.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> buscarPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    @GetMapping("/disponible/{disponible}")
    public List<Producto> buscarPorDisponible(@PathVariable Boolean disponible) {
        return productoService.buscarPorDisponible(disponible);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Producto> buscarPorNombre(@PathVariable String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/bajo-stock")
    public List<Producto> productosConBajoStock() {
        return productoService.productosConBajoStock();
    }

    @PostMapping
    public Producto guardarProducto(@Valid @RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }

    @PutMapping("/{id}")
    public Producto modificarProducto(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto
    ) {
        return productoService.modificarProducto(id, producto);
    }

    @PutMapping("/aumentar/{id}/{cantidad}")
    public Producto aumentarStock(
            @PathVariable Long id,
            @PathVariable Integer cantidad
    ) {
        return productoService.aumentarStock(id, cantidad);
    }

    @PutMapping("/disminuir/{id}/{cantidad}")
    public Producto disminuirStock(
            @PathVariable Long id,
            @PathVariable Integer cantidad
    ) {
        return productoService.disminuirStock(id, cantidad);
    }

    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "Producto eliminado correctamente";
    }
}
