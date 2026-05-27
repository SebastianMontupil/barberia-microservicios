package com.barberia.resenas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

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

    @Column(name = "barbero_id")
    private Long barberoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "barbero_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BarberoReferencia barbero;

    private Integer calificacion;
    // 1 a 5

    private String comentario;

    private LocalDate fechaResena;
}
