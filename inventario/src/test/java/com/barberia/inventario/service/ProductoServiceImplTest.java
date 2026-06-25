package com.barberia.inventario.service;

import com.barberia.inventario.dto.ProductoRequestDTO;
import com.barberia.inventario.dto.ProductoResponseDTO;
import com.barberia.inventario.exception.ResourceNotFoundException;
import com.barberia.inventario.model.Producto;
import com.barberia.inventario.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ProductoServiceImpl productoService;
    private Producto producto;
    private ProductoRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        producto = Producto.builder().id(1L).nombre("Cera para cabello").descripcion("Fijacion media")
                .stock(10).stockMinimo(3).precio(8000.0).build();
        request = ProductoRequestDTO.builder().nombre("Cera para cabello").descripcion("Fijacion media")
                .stock(10).stockMinimo(3).precio(8000.0).build();
    }

    @Test
    void listarProductos_deberiaRetornarLista() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.listarProductos();

        assertEquals(1, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO resultado = productoService.buscarPorId(1L);

        assertEquals("Cera para cabello", resultado.getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.buscarPorId(99L));
    }

    @Test
    void guardarProducto_deberiaGuardarProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO resultado = productoService.guardarProducto(request);

        assertEquals(1L, resultado.getId());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void modificarProducto_cuandoExiste_deberiaActualizarProducto() {
        ProductoRequestDTO actualizado = ProductoRequestDTO.builder().nombre("Cera premium").descripcion("Fijacion alta")
                .stock(12).stockMinimo(3).precio(10000.0).build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponseDTO resultado = productoService.modificarProducto(1L, actualizado);

        assertEquals("Cera premium", resultado.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void eliminarProducto_cuandoExiste_deberiaEliminarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.eliminarProducto(1L);

        verify(productoRepository).delete(producto);
    }
}
