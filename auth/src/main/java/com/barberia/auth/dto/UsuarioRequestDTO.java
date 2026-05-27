package com.barberia.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}