package com.barberia.reportes.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {

    private Long id;

    private Long clienteId;
    private String nombreCliente;

    private Long barberoId;
    private String nombreBarbero;

    private LocalDate fecha;
    private LocalTime hora;

    private String estado;
}