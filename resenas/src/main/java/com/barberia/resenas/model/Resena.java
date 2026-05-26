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

    private Long clienteId;

    private Long barberoId;

    private Integer calificacion;
    // 1 a 5

    private String comentario;

    private LocalDate fechaResena;
}