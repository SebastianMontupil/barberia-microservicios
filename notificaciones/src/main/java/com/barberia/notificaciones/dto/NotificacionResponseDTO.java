package com.barberia.notificaciones.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponseDTO {

    private Long id;

    private Long usuarioId;
    private String nombreUsuario;
    private String emailUsuario;
    private String telefonoUsuario;

    private Long agendaId;
    private LocalDate fechaAgenda;
    private LocalTime horaAgenda;
    private String nombreBarbero;

    private String tipo;
    private String mensaje;
    private String estado;

    private LocalDateTime fechaEnvio;
}
