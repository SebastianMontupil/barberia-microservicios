package com.barberia.resenas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El barberoId es obligatorio")
    private Long barberoId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion debe estar entre 1 y 5")
    @Max(value = 5, message = "La calificacion debe estar entre 1 y 5")
    private Integer calificacion;

    private String comentario;
}
