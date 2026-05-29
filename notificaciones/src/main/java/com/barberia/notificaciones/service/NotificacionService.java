package com.barberia.notificaciones.service;

import com.barberia.notificaciones.dto.NotificacionRequestDTO;
import com.barberia.notificaciones.dto.NotificacionResponseDTO;

import java.util.List;

public interface NotificacionService {

    List<NotificacionResponseDTO> listarNotificaciones();

    NotificacionResponseDTO buscarPorId(Long id);

    List<NotificacionResponseDTO> buscarPorUsuarioId(Long usuarioId);

    List<NotificacionResponseDTO> buscarPorAgendaId(Long agendaId);

    List<NotificacionResponseDTO> buscarPorTipo(String tipo);

    NotificacionResponseDTO crearNotificacion(NotificacionRequestDTO dto);

    NotificacionResponseDTO actualizarNotificacion(Long id, NotificacionRequestDTO dto);

    void eliminarNotificacion(Long id);
}
