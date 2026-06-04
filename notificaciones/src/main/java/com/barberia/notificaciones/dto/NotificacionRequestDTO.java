package com.barberia.notificaciones.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    @Min(value = 1, message = "El usuarioId debe ser mayor a 0")
    private Long usuarioId;

    @NotNull(message = "El agendaId es obligatorio")
    @Min(value = 1, message = "El agendaId debe ser mayor a 0")
    private Long agendaId;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String mensaje;

    @NotNull(message = "La fechaEnvio es obligatoria")
    @FutureOrPresent(message = "La fechaEnvio no puede estar en el pasado")
    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El tipo de notificacion es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;
}
