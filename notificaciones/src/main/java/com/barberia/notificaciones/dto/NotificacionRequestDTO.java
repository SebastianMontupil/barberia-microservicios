package com.barberia.notificaciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequestDTO {

    private Long usuarioId;
    private Long agendaId;
    private String tipo;
    private String mensaje;
    private String estado;
}