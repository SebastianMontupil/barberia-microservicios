package com.barberia.pagos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoRequestDTO {

    @NotNull(message = "El agendaId es obligatorio")
    private Long agendaId;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "La fechaPago es obligatoria")
    private LocalDateTime fechaPago;

    @NotBlank(message = "El estadoPago es obligatorio")
    @Size(max = 30, message = "El estadoPago no puede superar los 30 caracteres")
    private String estadoPago;

    @NotBlank(message = "El metodoPago es obligatorio")
    @Size(max = 50, message = "El metodoPago no puede superar los 50 caracteres")
    private String metodoPago;
}
