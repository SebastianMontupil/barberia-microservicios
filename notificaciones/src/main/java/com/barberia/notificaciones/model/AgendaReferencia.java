package com.barberia.notificaciones.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendas_referencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaReferencia {

    @Id
    private Long id;

    private Long clienteId;

    private Long barberoId;

    private LocalDate fecha;

    private LocalTime hora;

    private String estado;
}
