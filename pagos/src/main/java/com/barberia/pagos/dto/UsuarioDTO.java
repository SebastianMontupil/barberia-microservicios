package com.barberia.pagos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String rol;
}