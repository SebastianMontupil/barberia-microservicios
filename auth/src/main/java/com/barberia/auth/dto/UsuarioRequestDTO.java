package com.barberia.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Size(min = 6, max = 100, message = "La password debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 8, max = 20, message = "El telefono debe tener entre 8 y 20 caracteres")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El telefono debe contener solo numeros y puede iniciar con +")
    private String telefono;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(CLIENTE|BARBERO|ADMIN)$", message = "El rol debe ser CLIENTE, BARBERO o ADMIN")
    private String rol;
}
