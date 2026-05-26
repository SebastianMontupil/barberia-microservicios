package com.barberia.pagos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private Long agendaId;

    private Integer monto;

    private String metodoPago;

    private String estadoPago;

    private LocalDate fechaPago;
}