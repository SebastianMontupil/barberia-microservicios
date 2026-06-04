package com.barberia.inventario.service;

import com.barberia.inventario.dto.ProductoRequestDTO;
import com.barberia.inventario.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoResponseDTO> listarProductos();

    ProductoResponseDTO buscarPorId(Long id);

    List<ProductoResponseDTO> buscarPorNombre(String nombre);

    List<ProductoResponseDTO> buscarPorDisponible(Boolean disponible);

    List<ProductoResponseDTO> productosConBajoStock();

    Boolean verificarDisponibilidad(Long id);

    ProductoResponseDTO guardarProducto(ProductoRequestDTO dto);

    ProductoResponseDTO modificarProducto(Long id, ProductoRequestDTO dto);

    ProductoResponseDTO aumentarStock(Long id, Integer cantidad);

    ProductoResponseDTO disminuirStock(Long id, Integer cantidad);

    void eliminarProducto(Long id);
}
