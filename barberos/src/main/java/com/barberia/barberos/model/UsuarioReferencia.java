package com.barberia.barberos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios_referencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioReferencia {

    @Id
    private Long id;

    private String nombre;

    private String email;

    private String telefono;

    private String rol;
}
