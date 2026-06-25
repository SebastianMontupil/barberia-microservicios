package com.barberia.resenas.service;

import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;
import com.barberia.resenas.exception.ResourceNotFoundException;
import com.barberia.resenas.model.Resena;
import com.barberia.resenas.repository.ResenaRepository;
import java.time.LocalDateTime;
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
class ResenaServiceImplTest {

    @Mock private ResenaRepository resenaRepository;
    @InjectMocks private ResenaServiceImpl resenaService;
    private Resena resena;
    private ResenaRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 20, 13, 0);
        resena = Resena.builder().id(1L).clienteId(2L).barberoId(3L).calificacion(5)
                .comentario("Excelente atencion").fecha(fecha).build();
        request = ResenaRequestDTO.builder().clienteId(2L).barberoId(3L).calificacion(5)
                .comentario("Excelente atencion").fecha(fecha).build();
    }

    @Test
    void listarResenas_deberiaRetornarLista() {
        when(resenaRepository.findAll()).thenReturn(List.of(resena));

        List<ResenaResponseDTO> resultado = resenaService.listarResenas();

        assertEquals(1, resultado.size());
        verify(resenaRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarResena() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        ResenaResponseDTO resultado = resenaService.buscarPorId(1L);

        assertEquals(5, resultado.getCalificacion());
        verify(resenaRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resenaService.buscarPorId(99L));
    }

    @Test
    void guardarResena_deberiaGuardarResena() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        ResenaResponseDTO resultado = resenaService.guardarResena(request);

        assertEquals(1L, resultado.getId());
        verify(resenaRepository).save(any(Resena.class));
    }

    @Test
    void modificarResena_cuandoExiste_deberiaActualizarResena() {
        ResenaRequestDTO actualizada = ResenaRequestDTO.builder().clienteId(2L).barberoId(3L).calificacion(4)
                .comentario("Muy buen servicio").fecha(request.getFecha()).build();
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));
        when(resenaRepository.save(any(Resena.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResenaResponseDTO resultado = resenaService.modificarResena(1L, actualizada);

        assertEquals(4, resultado.getCalificacion());
        verify(resenaRepository).save(resena);
    }

    @Test
    void eliminarResena_cuandoExiste_deberiaEliminarResena() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        resenaService.eliminarResena(1L);

        verify(resenaRepository).delete(resena);
    }
}
