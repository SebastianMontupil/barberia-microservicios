package com.barberia.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UsuarioReferencia usuario;

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

    private String tipo;
    // CORREO, SMS, WHATSAPP

    private String mensaje;

    private String estado;
    // PENDIENTE, ENVIADA, ERROR

    private LocalDateTime fechaEnvio;
}
