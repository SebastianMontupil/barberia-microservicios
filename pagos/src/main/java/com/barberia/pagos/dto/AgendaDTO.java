package com.barberia.pagos.dto;

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
    private String emailCliente;
    private String telefonoCliente;

    private Long barberoId;
    private String nombreBarbero;
    private String especialidadBarbero;

    private LocalDate fecha;
    private LocalTime hora;

    private String estado;
}