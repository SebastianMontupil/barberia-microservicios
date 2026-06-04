package com.barberia.agendas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class AgendaRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    @Min(value = 1, message = "El clienteId debe ser mayor a 0")
    private Long clienteId;

    @NotNull(message = "El barberoId es obligatorio")
    @Min(value = 1, message = "El barberoId debe ser mayor a 0")
    private Long barberoId;

    @NotNull(message = "El servicioId es obligatorio")
    @Min(value = 1, message = "El servicioId debe ser mayor a 0")
    private Long servicioId;

    @NotNull(message = "La fechaHora es obligatoria")
    @FutureOrPresent(message = "La fechaHora no puede estar en el pasado")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    @Pattern(
            regexp = "RESERVADA|CANCELADA|REPROGRAMADA",
            message = "El estado debe ser RESERVADA, CANCELADA o REPROGRAMADA"
    )
    private String estado;
}
