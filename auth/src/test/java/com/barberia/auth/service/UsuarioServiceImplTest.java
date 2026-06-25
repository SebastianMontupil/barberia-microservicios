package com.barberia.auth.service;

import com.barberia.auth.dto.UsuarioRequestDTO;
import com.barberia.auth.dto.UsuarioResponseDTO;
import com.barberia.auth.model.Usuario;
import com.barberia.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Ana Pérez")
                .email("ana@barberia.cl")
                .password("hash")
                .telefono("+56912345678")
                .rol("CLIENTE")
                .build();

        request = UsuarioRequestDTO.builder()
                .nombre("Ana Pérez")
                .email("ana@barberia.cl")
                .password("secreta")
                .telefono("+56912345678")
                .rol("CLIENTE")
                .build();
    }

    @Test
    void debeListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("Ana Pérez", resultado.getFirst().getNombre());
        verify(usuarioRepository).findAll();
    }

    @Test
    void debeBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        assertEquals("ana@barberia.cl", resultado.getEmail());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void debeCrearUsuario() {
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("secreta")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(request);

        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void debeActualizarUsuarioExistente() {
        UsuarioRequestDTO actualizado = UsuarioRequestDTO.builder()
                .nombre("Ana María Pérez")
                .email("ana@barberia.cl")
                .password("nueva-clave")
                .telefono("+56912345678")
                .rol("CLIENTE")
                .build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail(actualizado.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva-clave")).thenReturn("nuevo-hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO resultado = usuarioService.actualizarUsuario(1L, actualizado);

        assertEquals("Ana María Pérez", resultado.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void debeEliminarUsuarioExistente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository).delete(usuario);
    }
}
