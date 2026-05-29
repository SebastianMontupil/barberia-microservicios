package com.barberia.agendas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicios_referencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioReferencia {

    @Id
    private Long id;

    private String nombre;

    private String descripcion;

    private Integer precio;

    private Integer duracionMinutos;

    private Boolean disponible;
}
