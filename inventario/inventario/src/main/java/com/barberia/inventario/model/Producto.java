package com.barberia.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String categoria;
    // GEL, SHAMPOO, CERA, ACEITE, OTRO

    private Integer stock;

    private Integer stockMinimo;

    private Integer precio;

    private Boolean disponible;
}