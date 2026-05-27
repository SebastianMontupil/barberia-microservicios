package com.barberia.notificaciones.service;

import com.barberia.notificaciones.dto.AgendaDTO;
import com.barberia.notificaciones.dto.NotificacionRequestDTO;
import com.barberia.notificaciones.dto.NotificacionResponseDTO;
import com.barberia.notificaciones.dto.UsuarioDTO;
import com.barberia.notificaciones.model.Notificacion;
import com.barberia.notificaciones.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final RestTemplate restTemplate;

    public NotificacionService(NotificacionRepository notificacionRepository, RestTemplate restTemplate) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = restTemplate;
    }

    public List<NotificacionResponseDTO> listarNotificaciones() {
        return notificacionRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificacionResponseDTO> buscarPorId(Long id) {
        return notificacionRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public List<NotificacionResponseDTO> buscarPorUsuarioId(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> buscarPorAgendaId(Long agendaId) {
        return notificacionRepository.findByAgendaId(agendaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> buscarPorTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> buscarPorEstado(String estado) {
        return notificacionRepository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificacionResponseDTO crearNotificacion(NotificacionRequestDTO dto) {

        Notificacion notificacion = new Notificacion();

        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setAgendaId(dto.getAgendaId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());

        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            notificacion.setEstado("PENDIENTE");
        } else {
            notificacion.setEstado(dto.getEstado());
        }

        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        return convertirAResponseDTO(notificacionGuardada);
    }

    public NotificacionResponseDTO marcarComoEnviada(Long id) {

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setEstado("ENVIADA");
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);

        return convertirAResponseDTO(notificacionActualizada);
    }

    public NotificacionResponseDTO marcarComoError(Long id) {

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setEstado("ERROR");
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion notificacionActualizada = notificacionRepository.save(notificacion);

        return convertirAResponseDTO(notificacionActualizada);
    }

    public void eliminarNotificacion(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }

        notificacionRepository.deleteById(id);
    }

    private NotificacionResponseDTO convertirAResponseDTO(Notificacion notificacion) {

        NotificacionResponseDTO dto = new NotificacionResponseDTO();

        dto.setId(notificacion.getId());
        dto.setUsuarioId(notificacion.getUsuarioId());
        dto.setAgendaId(notificacion.getAgendaId());
        dto.setTipo(notificacion.getTipo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setEstado(notificacion.getEstado());
        dto.setFechaEnvio(notificacion.getFechaEnvio());

        try {
            UsuarioDTO usuario = restTemplate.getForObject(
                    "http://localhost:8081/api/usuarios/" + notificacion.getUsuarioId(),
                    UsuarioDTO.class
            );

            if (usuario != null) {
                dto.setNombreUsuario(usuario.getNombre());
                dto.setEmailUsuario(usuario.getEmail());
                dto.setTelefonoUsuario(usuario.getTelefono());
            }

        } catch (Exception e) {
            dto.setNombreUsuario("Usuario no encontrado");
            dto.setEmailUsuario("Sin información");
            dto.setTelefonoUsuario("Sin información");
        }

        try {
            AgendaDTO agenda = restTemplate.getForObject(
                    "http://localhost:8083/api/agendas/" + notificacion.getAgendaId(),
                    AgendaDTO.class
            );

            if (agenda != null) {
                dto.setFechaAgenda(agenda.getFecha());
                dto.setHoraAgenda(agenda.getHora());
                dto.setNombreBarbero(agenda.getNombreBarbero());
            }

        } catch (Exception e) {
            dto.setNombreBarbero("Agenda no encontrada");
        }

        return dto;
    }
}
