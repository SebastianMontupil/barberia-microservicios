package com.barberia.inventario.service;

import com.barberia.inventario.dto.ProductoRequestDTO;
import com.barberia.inventario.dto.ProductoResponseDTO;
import com.barberia.inventario.exception.ResourceNotFoundException;
import com.barberia.inventario.model.Producto;
import com.barberia.inventario.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {
        log.info("Iniciando listado de productos");

        try {
            List<ProductoResponseDTO> productos = productoRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Listado de productos finalizado. Total: {}", productos.size());
            return productos;
        } catch (DataAccessException ex) {
            log.error("Error al listar productos", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de producto por id: {}", id);

        try {
            Producto producto = obtenerProductoPorId(id);
            ProductoResponseDTO response = convertirAResponseDTO(producto);

            log.info("Busqueda de producto por id finalizada: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro producto con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al buscar producto por id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        log.info("Iniciando busqueda de productos por nombre: {}", nombre);

        try {
            List<ProductoResponseDTO> productos = productoRepository.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por nombre finalizada. nombre={}, total={}", nombre, productos.size());
            return productos;
        } catch (DataAccessException ex) {
            log.error("Error al buscar productos por nombre: {}", nombre, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorDisponible(Boolean disponible) {
        log.info("Iniciando busqueda de productos por disponibilidad calculada: {}", disponible);

        try {
            List<ProductoResponseDTO> productos = productoRepository.findAll()
                    .stream()
                    .filter(producto -> productoDisponible(producto).equals(disponible))
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por disponibilidad finalizada. disponible={}, total={}", disponible, productos.size());
            return productos;
        } catch (DataAccessException ex) {
            log.error("Error al buscar productos por disponibilidad: {}", disponible, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> productosConBajoStock() {
        log.info("Iniciando busqueda de productos con bajo stock");

        try {
            List<ProductoResponseDTO> productos = productoRepository.findAll()
                    .stream()
                    .filter(producto -> producto.getStock() <= producto.getStockMinimo())
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda de productos con bajo stock finalizada. Total: {}", productos.size());
            return productos;
        } catch (DataAccessException ex) {
            log.error("Error al buscar productos con bajo stock", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean verificarDisponibilidad(Long id) {
        log.info("Iniciando verificacion de disponibilidad para producto id: {}", id);

        try {
            Producto producto = obtenerProductoPorId(id);
            Boolean disponible = productoDisponible(producto);

            log.info("Verificacion de disponibilidad finalizada. id={}, disponible={}", id, disponible);
            return disponible;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo verificar disponibilidad. Producto no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al verificar disponibilidad del producto id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO guardarProducto(ProductoRequestDTO dto) {
        log.info("Iniciando creacion de producto: {}", dto.getNombre());

        try {
            Producto producto = convertirAEntidad(dto);
            Producto productoGuardado = productoRepository.save(producto);
            ProductoResponseDTO response = convertirAResponseDTO(productoGuardado);

            log.info("Creacion de producto finalizada. id={}, nombre={}", productoGuardado.getId(), productoGuardado.getNombre());
            return response;
        } catch (DataAccessException ex) {
            log.error("Error al crear producto: {}", dto.getNombre(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO modificarProducto(Long id, ProductoRequestDTO dto) {
        log.info("Iniciando actualizacion de producto id: {}", id);

        try {
            Producto producto = obtenerProductoPorId(id);

            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setStock(dto.getStock());
            producto.setStockMinimo(dto.getStockMinimo());
            producto.setPrecio(dto.getPrecio());

            Producto productoActualizado = productoRepository.save(producto);
            ProductoResponseDTO response = convertirAResponseDTO(productoActualizado);

            log.info("Actualizacion de producto finalizada. id={}, nombre={}", id, productoActualizado.getNombre());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo actualizar. Producto no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al actualizar producto id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO aumentarStock(Long id, Integer cantidad) {
        log.info("Iniciando aumento de stock para producto id: {}. cantidad={}", id, cantidad);

        validarCantidad(cantidad);

        try {
            Producto producto = obtenerProductoPorId(id);
            producto.setStock(producto.getStock() + cantidad);

            Producto productoActualizado = productoRepository.save(producto);
            ProductoResponseDTO response = convertirAResponseDTO(productoActualizado);

            log.info("Aumento de stock finalizado. id={}, stock={}", id, productoActualizado.getStock());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo aumentar stock. Producto no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al aumentar stock del producto id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO disminuirStock(Long id, Integer cantidad) {
        log.info("Iniciando disminucion de stock para producto id: {}. cantidad={}", id, cantidad);

        validarCantidad(cantidad);

        try {
            Producto producto = obtenerProductoPorId(id);

            if (producto.getStock() < cantidad) {
                log.warn("Stock insuficiente para producto id: {}. stock={}, cantidad={}", id, producto.getStock(), cantidad);
                throw new IllegalArgumentException("No hay suficiente stock disponible");
            }

            producto.setStock(producto.getStock() - cantidad);

            Producto productoActualizado = productoRepository.save(producto);
            ProductoResponseDTO response = convertirAResponseDTO(productoActualizado);

            log.info("Disminucion de stock finalizada. id={}, stock={}", id, productoActualizado.getStock());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo disminuir stock. Producto no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al disminuir stock del producto id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        log.info("Iniciando eliminacion de producto id: {}", id);

        try {
            Producto producto = obtenerProductoPorId(id);
            productoRepository.delete(producto);

            log.info("Eliminacion de producto finalizada. id={}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo eliminar. Producto no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al eliminar producto id: {}", id, ex);
            throw ex;
        }
    }

    private Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            log.warn("Cantidad invalida para operacion de stock: {}", cantidad);
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
    }

    private Boolean productoDisponible(Producto producto) {
        return producto.getStock() > 0;
    }

    private Producto convertirAEntidad(ProductoRequestDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .stock(dto.getStock())
                .stockMinimo(dto.getStockMinimo())
                .precio(dto.getPrecio())
                .build();
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .stockMinimo(producto.getStockMinimo())
                .precio(producto.getPrecio())
                .build();
    }
}
