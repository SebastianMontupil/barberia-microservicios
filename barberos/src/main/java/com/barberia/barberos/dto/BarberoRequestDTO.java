package com.barberia.barberos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberoRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    @Min(value = 1, message = "El usuarioId debe ser mayor que cero")
    private Long usuarioId;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(min = 2, max = 100, message = "La especialidad debe tener entre 2 y 100 caracteres")
    private String especialidad;

    @NotBlank(message = "El horario es obligatorio")
    @Size(min = 3, max = 120, message = "El horario debe tener entre 3 y 120 caracteres")
    private String horario;

    @NotNull(message = "Los aniosExperiencia son obligatorios")
    @Min(value = 0, message = "Los aniosExperiencia no pueden ser negativos")
    private Integer aniosExperiencia;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;
}
