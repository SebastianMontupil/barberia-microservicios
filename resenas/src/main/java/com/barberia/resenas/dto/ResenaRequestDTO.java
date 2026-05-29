package com.barberia.resenas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
public class ResenaRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    @Min(value = 1, message = "El clienteId debe ser mayor a 0")
    private Long clienteId;

    @NotNull(message = "El barberoId es obligatorio")
    @Min(value = 1, message = "El barberoId debe ser mayor a 0")
    private Long barberoId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion debe estar entre 1 y 5")
    @Max(value = 5, message = "La calificacion debe estar entre 1 y 5")
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comentario;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede estar en el futuro")
    private LocalDateTime fecha;
}
