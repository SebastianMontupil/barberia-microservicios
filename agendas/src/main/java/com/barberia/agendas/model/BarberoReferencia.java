package com.barberia.agendas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "barberos_referencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberoReferencia {

    @Id
    private Long id;

    private Long usuarioId;

    private String nombre;

    private String especialidad;

    private Boolean disponible;
}
