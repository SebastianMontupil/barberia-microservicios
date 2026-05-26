package com.barberia.resenas.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaResponseDTO {

    private Long id;

    private Long clienteId;
    private String nombreCliente;
    private String emailCliente;

    private Long barberoId;
    private String nombreBarbero;
    private String especialidadBarbero;

    private Integer calificacion;
    private String comentario;
    private LocalDate fechaResena;
}