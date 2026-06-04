package com.barberia.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoResponseDTO {

    private Long id;

    private Long agendaId;

    private Long clienteId;

    private Double monto;

    private LocalDateTime fechaPago;

    private String estadoPago;

    private String metodoPago;
}
