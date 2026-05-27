package com.barberia.barberos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barbero {

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

    private String especialidad;

    private String horario;

    private Integer aniosExperiencia;

    private Boolean disponible;
}
