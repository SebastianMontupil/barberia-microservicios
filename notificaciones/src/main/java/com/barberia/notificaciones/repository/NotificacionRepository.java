package com.barberia.notificaciones.repository;

import com.barberia.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioId(Long usuarioId);

    List<Notificacion> findByAgendaId(Long agendaId);

    List<Notificacion> findByTipo(String tipo);
}
