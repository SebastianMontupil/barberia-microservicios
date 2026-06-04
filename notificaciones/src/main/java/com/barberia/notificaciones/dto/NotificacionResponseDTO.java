package com.barberia.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long agendaId;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String tipo;
}
