package com.barberia.pagos.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDTO {

    private Long clienteId;

    private Long agendaId;

    private Integer monto;

    private String metodoPago;

    private String estadoPago;

    private LocalDate fechaPago;
}