package com.barberia.pagos.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {

    private Long id;

    private Long clienteId;
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;

    private Long agendaId;
    private LocalDate fechaAgenda;
    private LocalTime horaAgenda;
    private String estadoAgenda;

    private String nombreBarbero;

    private Integer monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDate fechaPago;
}