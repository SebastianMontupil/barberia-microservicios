package com.barberia.auth.service;

import com.barberia.auth.dto.LoginRequestDTO;
import com.barberia.auth.dto.LoginResponseDTO;
import com.barberia.auth.dto.RecuperarPasswordDTO;
import com.barberia.auth.dto.UsuarioRequestDTO;
import com.barberia.auth.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    List<UsuarioResponseDTO> listarUsuarios();

    UsuarioResponseDTO buscarPorId(Long id);

    UsuarioResponseDTO buscarPorEmail(String email);

    List<UsuarioResponseDTO> buscarPorRol(String rol);

    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

    String recuperarPassword(RecuperarPasswordDTO dto);

    void eliminarUsuario(Long id);
}
