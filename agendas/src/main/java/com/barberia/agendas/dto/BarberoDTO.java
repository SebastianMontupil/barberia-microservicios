package com.barberia.agendas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberoDTO {

    private Long id;
    private Long usuarioId;

    private String nombre;
    private String email;
    private String telefono;

    private String especialidad;
    private String horario;
    private Integer aniosExperiencia;
    private Boolean disponible;
}