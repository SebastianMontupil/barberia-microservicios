package com.barberia.notificaciones.service;

import com.barberia.notificaciones.dto.NotificacionRequestDTO;
import com.barberia.notificaciones.dto.NotificacionResponseDTO;
import com.barberia.notificaciones.exception.ResourceNotFoundException;
import com.barberia.notificaciones.model.Notificacion;
import com.barberia.notificaciones.repository.NotificacionRepository;
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
class NotificacionServiceImplTest {

    @Mock private NotificacionRepository notificacionRepository;
    @InjectMocks private NotificacionServiceImpl notificacionService;
    private Notificacion notificacion;
    private NotificacionRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        LocalDateTime fechaEnvio = LocalDateTime.of(2026, 7, 1, 9, 0);
        notificacion = Notificacion.builder().id(1L).usuarioId(2L).agendaId(3L)
                .mensaje("Tu cita esta confirmada").fechaEnvio(fechaEnvio).tipo("CONFIRMACION").build();
        request = NotificacionRequestDTO.builder().usuarioId(2L).agendaId(3L)
                .mensaje("Tu cita esta confirmada").fechaEnvio(fechaEnvio).tipo("CONFIRMACION").build();
    }

    @Test
    void listarNotificaciones_deberiaRetornarLista() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

        List<NotificacionResponseDTO> resultado = notificacionService.listarNotificaciones();

        assertEquals(1, resultado.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarNotificacion() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        NotificacionResponseDTO resultado = notificacionService.buscarPorId(1L);

        assertEquals("CONFIRMACION", resultado.getTipo());
        verify(notificacionRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificacionService.buscarPorId(99L));
    }

    @Test
    void crearNotificacion_deberiaGuardarNotificacion() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        NotificacionResponseDTO resultado = notificacionService.crearNotificacion(request);

        assertEquals(1L, resultado.getId());
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void actualizarNotificacion_cuandoExiste_deberiaActualizarNotificacion() {
        NotificacionRequestDTO actualizada = NotificacionRequestDTO.builder().usuarioId(2L).agendaId(3L)
                .mensaje("Tu cita fue modificada").fechaEnvio(request.getFechaEnvio()).tipo("RECORDATORIO").build();
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificacionResponseDTO resultado = notificacionService.actualizarNotificacion(1L, actualizada);

        assertEquals("RECORDATORIO", resultado.getTipo());
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void eliminarNotificacion_cuandoExiste_deberiaEliminarNotificacion() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        notificacionService.eliminarNotificacion(1L);

        verify(notificacionRepository).delete(notificacion);
    }
}
