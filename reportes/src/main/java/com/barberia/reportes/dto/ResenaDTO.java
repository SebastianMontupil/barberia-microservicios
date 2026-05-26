package com.barberia.reportes.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {

    private Long id;

    private Long clienteId;
    private String nombreCliente;

    private Long barberoId;
    private String nombreBarbero;

    private Integer calificacion;
    private String comentario;
    private LocalDate fechaResena;
}