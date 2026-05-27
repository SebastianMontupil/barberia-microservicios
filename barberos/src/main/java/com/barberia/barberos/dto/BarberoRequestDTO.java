package com.barberia.barberos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberoRequestDTO {

    private Long usuarioId;
    private String especialidad;
    private String horario;
    private Integer aniosExperiencia;
    private Boolean disponible;
}