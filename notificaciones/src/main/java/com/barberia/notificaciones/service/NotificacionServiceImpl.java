package com.barberia.notificaciones.service;

import com.barberia.notificaciones.dto.NotificacionRequestDTO;
import com.barberia.notificaciones.dto.NotificacionResponseDTO;
import com.barberia.notificaciones.exception.ResourceNotFoundException;
import com.barberia.notificaciones.model.Notificacion;
import com.barberia.notificaciones.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarNotificaciones() {
        log.info("Iniciando listado de notificaciones");
        try {
            List<NotificacionResponseDTO> notificaciones = notificacionRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();
            log.info("Listado de notificaciones finalizado. Total: {}", notificaciones.size());
            return notificaciones;
        } catch (RuntimeException ex) {
            log.error("Error al listar notificaciones", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificacionResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de notificacion por id: {}", id);
        try {
            Notificacion notificacion = obtenerNotificacionPorId(id);
            log.info("Busqueda de notificacion por id finalizada: {}", id);
            return convertirAResponseDTO(notificacion);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro notificacion con id: {}", id);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al buscar notificacion con id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> buscarPorUsuarioId(Long usuarioId) {
        log.info("Iniciando busqueda de notificaciones por usuarioId: {}", usuarioId);
        try {
            List<NotificacionResponseDTO> notificaciones = notificacionRepository.findByUsuarioId(usuarioId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();
            log.info("Busqueda por usuarioId finalizada: {}. Total: {}", usuarioId, notificaciones.size());
            return notificaciones;
        } catch (RuntimeException ex) {
            log.error("Error al buscar notificaciones por usuarioId: {}", usuarioId, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> buscarPorAgendaId(Long agendaId) {
        log.info("Iniciando busqueda de notificaciones por agendaId: {}", agendaId);
        try {
            List<NotificacionResponseDTO> notificaciones = notificacionRepository.findByAgendaId(agendaId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();
            log.info("Busqueda por agendaId finalizada: {}. Total: {}", agendaId, notificaciones.size());
            return notificaciones;
        } catch (RuntimeException ex) {
            log.error("Error al buscar notificaciones por agendaId: {}", agendaId, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> buscarPorTipo(String tipo) {
        log.info("Iniciando busqueda de notificaciones por tipo: {}", tipo);
        try {
            List<NotificacionResponseDTO> notificaciones = notificacionRepository.findByTipo(tipo)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();
            log.info("Busqueda por tipo finalizada: {}. Total: {}", tipo, notificaciones.size());
            return notificaciones;
        } catch (RuntimeException ex) {
            log.error("Error al buscar notificaciones por tipo: {}", tipo, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public NotificacionResponseDTO crearNotificacion(NotificacionRequestDTO dto) {
        log.info("Iniciando creacion de notificacion para usuarioId: {}, agendaId: {}",
                dto.getUsuarioId(), dto.getAgendaId());
        try {
            Notificacion notificacion = Notificacion.builder()
                    .usuarioId(dto.getUsuarioId())
                    .agendaId(dto.getAgendaId())
                    .mensaje(dto.getMensaje())
                    .fechaEnvio(dto.getFechaEnvio())
                    .tipo(dto.getTipo())
                    .build();

            Notificacion notificacionGuardada = notificacionRepository.save(notificacion);
            log.info("Creacion de notificacion finalizada con id: {}", notificacionGuardada.getId());
            return convertirAResponseDTO(notificacionGuardada);
        } catch (RuntimeException ex) {
            log.error("Error al crear notificacion para usuarioId: {}, agendaId: {}",
                    dto.getUsuarioId(), dto.getAgendaId(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public NotificacionResponseDTO actualizarNotificacion(Long id, NotificacionRequestDTO dto) {
        log.info("Iniciando actualizacion de notificacion con id: {}", id);
        try {
            Notificacion notificacion = obtenerNotificacionPorId(id);
            notificacion.setUsuarioId(dto.getUsuarioId());
            notificacion.setAgendaId(dto.getAgendaId());
            notificacion.setMensaje(dto.getMensaje());
            notificacion.setFechaEnvio(dto.getFechaEnvio());
            notificacion.setTipo(dto.getTipo());

            Notificacion notificacionActualizada = notificacionRepository.save(notificacion);
            log.info("Actualizacion de notificacion finalizada con id: {}", notificacionActualizada.getId());
            return convertirAResponseDTO(notificacionActualizada);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro notificacion para actualizar con id: {}", id);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al actualizar notificacion con id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void eliminarNotificacion(Long id) {
        log.info("Iniciando eliminacion de notificacion con id: {}", id);
        try {
            Notificacion notificacion = obtenerNotificacionPorId(id);
            notificacionRepository.delete(notificacion);
            log.info("Eliminacion de notificacion finalizada con id: {}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro notificacion para eliminar con id: {}", id);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al eliminar notificacion con id: {}", id, ex);
            throw ex;
        }
    }

    private Notificacion obtenerNotificacionPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion no encontrada con id: " + id));
    }

    private NotificacionResponseDTO convertirAResponseDTO(Notificacion notificacion) {
        return NotificacionResponseDTO.builder()
                .id(notificacion.getId())
                .usuarioId(notificacion.getUsuarioId())
                .agendaId(notificacion.getAgendaId())
                .mensaje(notificacion.getMensaje())
                .fechaEnvio(notificacion.getFechaEnvio())
                .tipo(notificacion.getTipo())
                .build();
    }
}
