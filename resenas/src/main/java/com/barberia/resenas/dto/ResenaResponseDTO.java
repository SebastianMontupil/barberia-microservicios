package com.barberia.resenas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaResponseDTO {

    private Long id;
    private Long clienteId;
    private Long barberoId;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fecha;
}
