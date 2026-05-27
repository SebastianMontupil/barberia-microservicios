package com.barberia.auth.controller;

import com.barberia.auth.dto.LoginRequestDTO;
import com.barberia.auth.dto.LoginResponseDTO;
import com.barberia.auth.dto.RecuperarPasswordDTO;
import com.barberia.auth.dto.UsuarioRequestDTO;
import com.barberia.auth.dto.UsuarioResponseDTO;
import com.barberia.auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Optional<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/email/{email}")
    public Optional<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }

    @GetMapping("/rol/{rol}")
    public List<UsuarioResponseDTO> buscarPorRol(@PathVariable String rol) {
        return usuarioService.buscarPorRol(rol);
    }

    @PostMapping
    public UsuarioResponseDTO registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.registrarUsuario(dto);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return usuarioService.login(dto);
    }

    @PutMapping("/recuperar-password")
    public String recuperarPassword(@Valid @RequestBody RecuperarPasswordDTO dto) {
        return usuarioService.recuperarPassword(dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "Usuario eliminado correctamente";
    }
}