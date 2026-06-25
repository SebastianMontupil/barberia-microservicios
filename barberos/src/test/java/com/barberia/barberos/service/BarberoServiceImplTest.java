package com.barberia.barberos.service;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.exception.ResourceNotFoundException;
import com.barberia.barberos.model.Barbero;
import com.barberia.barberos.repository.BarberoRepository;
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
class BarberoServiceImplTest {

    @Mock private BarberoRepository barberoRepository;
    @InjectMocks private BarberoServiceImpl barberoService;
    private Barbero barbero;
    private BarberoRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        barbero = Barbero.builder().id(1L).usuarioId(10L).especialidad("Cortes clasicos")
                .horario("09:00 - 18:00").aniosExperiencia(5).disponible(true).build();
        request = BarberoRequestDTO.builder().usuarioId(10L).especialidad("Cortes clasicos")
                .horario("09:00 - 18:00").aniosExperiencia(5).disponible(true).build();
    }

    @Test
    void listarBarberos_deberiaRetornarLista() {
        when(barberoRepository.findAll()).thenReturn(List.of(barbero));

        List<BarberoResponseDTO> resultado = barberoService.listarBarberos();

        assertEquals(1, resultado.size());
        assertEquals("Cortes clasicos", resultado.getFirst().getEspecialidad());
        verify(barberoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarBarbero() {
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));

        BarberoResponseDTO resultado = barberoService.buscarPorId(1L);

        assertEquals(10L, resultado.getUsuarioId());
        verify(barberoRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(barberoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberoService.buscarPorId(99L));

        verify(barberoRepository).findById(99L);
    }

    @Test
    void guardarBarbero_deberiaGuardarBarbero() {
        when(barberoRepository.save(any(Barbero.class))).thenReturn(barbero);

        BarberoResponseDTO resultado = barberoService.guardarBarbero(request);

        assertEquals(1L, resultado.getId());
        verify(barberoRepository).save(any(Barbero.class));
    }

    @Test
    void modificarBarbero_cuandoExiste_deberiaActualizarBarbero() {
        BarberoRequestDTO actualizado = BarberoRequestDTO.builder().usuarioId(10L).especialidad("Barbas")
                .horario("10:00 - 19:00").aniosExperiencia(6).disponible(false).build();
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));
        when(barberoRepository.save(any(Barbero.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BarberoResponseDTO resultado = barberoService.modificarBarbero(1L, actualizado);

        assertEquals("Barbas", resultado.getEspecialidad());
        verify(barberoRepository).save(barbero);
    }

    @Test
    void eliminarBarbero_cuandoExiste_deberiaEliminarBarbero() {
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));

        barberoService.eliminarBarbero(1L);

        verify(barberoRepository).delete(barbero);
    }
}
