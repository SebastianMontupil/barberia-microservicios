package com.barberia.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    private Long agendaId;

    private String tipo;
    // CORREO, SMS, WHATSAPP

    private String mensaje;

    private String estado;
    // PENDIENTE, ENVIADA, ERROR

    private LocalDateTime fechaEnvio;
}