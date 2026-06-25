package com.barberia.servicios.service;

import com.barberia.servicios.dto.ServicioRequestDTO;
import com.barberia.servicios.dto.ServicioResponseDTO;
import com.barberia.servicios.exception.ResourceNotFoundException;
import com.barberia.servicios.model.Servicio;
import com.barberia.servicios.repository.ServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    private Servicio servicio;
    private ServicioRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        servicio = Servicio.builder()
                .id(1L)
                .nombre("Corte de cabello")
                .descripcion("Corte clásico")
                .precio(12000.0)
                .duracion(45)
                .build();

        request = ServicioRequestDTO.builder()
                .nombre("Corte de cabello")
                .descripcion("Corte clásico")
                .precio(12000.0)
                .duracion(45)
                .build();
    }

    @Test
    void debeListarServicios() {
        when(servicioRepository.findAll()).thenReturn(List.of(servicio));

        List<ServicioResponseDTO> resultado = servicioService.listarServicios();

        assertEquals(1, resultado.size());
        assertEquals("Corte de cabello", resultado.getFirst().getNombre());
        verify(servicioRepository).findAll();
    }

    @Test
    void debeBuscarServicioPorId() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));

        ServicioResponseDTO resultado = servicioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(servicioRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> servicioService.buscarPorId(99L));

        verify(servicioRepository).findById(99L);
    }

    @Test
    void debeCrearServicio() {
        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicio);

        ServicioResponseDTO resultado = servicioService.guardarServicio(request);

        assertEquals(1L, resultado.getId());
        verify(servicioRepository).save(any(Servicio.class));
    }

    @Test
    void debeActualizarServicioExistente() {
        ServicioRequestDTO actualizado = ServicioRequestDTO.builder()
                .nombre("Corte premium")
                .descripcion("Corte y lavado")
                .precio(15000.0)
                .duracion(60)
                .build();
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(servicioRepository.save(any(Servicio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServicioResponseDTO resultado = servicioService.modificarServicio(1L, actualizado);

        assertEquals("Corte premium", resultado.getNombre());
        verify(servicioRepository).save(any(Servicio.class));
    }

    @Test
    void debeEliminarServicioExistente() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));

        servicioService.eliminarServicio(1L);

        verify(servicioRepository).delete(servicio);
    }
}
