package com.barberia.reportes.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberoDTO {

    private Long id;
    private Long usuarioId;

    private String nombre;
    private String especialidad;
    private Integer aniosExperiencia;
    private Boolean disponible;
}