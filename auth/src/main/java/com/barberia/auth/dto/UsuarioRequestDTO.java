package com.barberia.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String rol;
}