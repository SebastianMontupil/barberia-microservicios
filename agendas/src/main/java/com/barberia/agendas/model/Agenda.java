package com.barberia.agendas.model;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
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

    @Column(name = "barbero_id", nullable = false)
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

    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "servicio_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ServicioReferencia servicio;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 30)
    private String estado;
}
