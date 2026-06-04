package com.barberia.barberos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String especialidad;
    private String horario;
    private Integer aniosExperiencia;
    private Boolean disponible;
}
