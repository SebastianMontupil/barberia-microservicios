package com.barberia.resenas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaRequestDTO {

    private Long clienteId;
    private Long barberoId;
    private Integer calificacion;
    private String comentario;
}