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

    @Column(name = "cliente_id")
    private Long clienteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "cliente_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UsuarioReferencia cliente;

    @Column(name = "agenda_id")
    private Long agendaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "agenda_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AgendaReferencia agenda;

    private Integer monto;

    private String metodoPago;

    private String estadoPago;

    private LocalDate fechaPago;
}
