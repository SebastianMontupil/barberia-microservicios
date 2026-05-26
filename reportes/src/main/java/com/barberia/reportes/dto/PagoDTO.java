package com.barberia.reportes.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {

    private Long id;
    private Long clienteId;
    private Long agendaId;

    private Integer monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDate fechaPago;
}