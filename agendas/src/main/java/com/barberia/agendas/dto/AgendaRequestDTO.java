package com.barberia.agendas.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequestDTO {

    private Long clienteId;
    private Long barberoId;

    private LocalDate fecha;
    private LocalTime hora;

    private String estado;
}