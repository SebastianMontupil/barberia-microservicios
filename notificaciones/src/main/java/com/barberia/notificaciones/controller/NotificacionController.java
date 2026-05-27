package com.barberia.notificaciones.controller;

import com.barberia.notificaciones.dto.NotificacionRequestDTO;
import com.barberia.notificaciones.dto.NotificacionResponseDTO;
import com.barberia.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionResponseDTO> listarNotificaciones() {
        return notificacionService.listarNotificaciones();
    }

    @GetMapping("/{id}")
    public Optional<NotificacionResponseDTO> buscarPorId(@PathVariable Long id) {
        return notificacionService.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<NotificacionResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return notificacionService.buscarPorUsuarioId(usuarioId);
    }

    @GetMapping("/agenda/{agendaId}")
    public List<NotificacionResponseDTO> buscarPorAgendaId(@PathVariable Long agendaId) {
        return notificacionService.buscarPorAgendaId(agendaId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<NotificacionResponseDTO> buscarPorTipo(@PathVariable String tipo) {
        return notificacionService.buscarPorTipo(tipo);
    }

    @GetMapping("/estado/{estado}")
    public List<NotificacionResponseDTO> buscarPorEstado(@PathVariable String estado) {
        return notificacionService.buscarPorEstado(estado);
    }

    @PostMapping
    public NotificacionResponseDTO crearNotificacion(@Valid @RequestBody NotificacionRequestDTO dto) {
        return notificacionService.crearNotificacion(dto);
    }

    @PutMapping("/enviada/{id}")
    public NotificacionResponseDTO marcarComoEnviada(@PathVariable Long id) {
        return notificacionService.marcarComoEnviada(id);
    }

    @PutMapping("/error/{id}")
    public NotificacionResponseDTO marcarComoError(@PathVariable Long id) {
        return notificacionService.marcarComoError(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return "Notificación eliminada correctamente";
    }
}