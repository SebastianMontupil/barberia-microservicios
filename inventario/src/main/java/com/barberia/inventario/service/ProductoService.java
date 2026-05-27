package com.barberia.inventario.service;

import com.barberia.inventario.model.Producto;
import com.barberia.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarPorDisponible(Boolean disponible) {
        return productoRepository.findByDisponible(disponible);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContaining(nombre);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto modificarProducto(Long id, Producto productoNuevo) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoNuevo.getNombre());
        producto.setCategoria(productoNuevo.getCategoria());
        producto.setStock(productoNuevo.getStock());
        producto.setStockMinimo(productoNuevo.getStockMinimo());
        producto.setPrecio(productoNuevo.getPrecio());
        producto.setDisponible(productoNuevo.getDisponible());

        return productoRepository.save(producto);
    }

    public Producto aumentarStock(Long id, Integer cantidad) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(producto.getStock() + cantidad);

        return productoRepository.save(producto);
    }

    public Producto disminuirStock(Long id, Integer cantidad) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock disponible");
        }

        producto.setStock(producto.getStock() - cantidad);

        if (producto.getStock() <= 0) {
            producto.setDisponible(false);
        }

        return productoRepository.save(producto);
    }

    public List<Producto> productosConBajoStock() {
        return productoRepository.findAll()
                .stream()
                .filter(producto -> producto.getStock() <= producto.getStockMinimo())
                .toList();
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}