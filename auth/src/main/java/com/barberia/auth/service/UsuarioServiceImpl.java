package com.barberia.auth.service;

import com.barberia.auth.dto.LoginRequestDTO;
import com.barberia.auth.dto.LoginResponseDTO;
import com.barberia.auth.dto.RecuperarPasswordDTO;
import com.barberia.auth.dto.UsuarioRequestDTO;
import com.barberia.auth.dto.UsuarioResponseDTO;
import com.barberia.auth.exception.AuthBusinessException;
import com.barberia.auth.exception.ResourceNotFoundException;
import com.barberia.auth.model.Usuario;
import com.barberia.auth.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        log.info("Inicio listarUsuarios");

        try {
            List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Fin listarUsuarios - totalUsuarios={}", usuarios.size());
            return usuarios;
        } catch (RuntimeException ex) {
            log.error("Error al listar usuarios", ex);
            throw ex;
        }
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        log.info("Inicio buscarPorId - id={}", id);

        try {
            Usuario usuario = obtenerUsuarioPorId(id);
            UsuarioResponseDTO response = convertirAResponseDTO(usuario);

            log.info("Fin buscarPorId - id={}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("Usuario no encontrado por id - id={}", id);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al buscar usuario por id - id={}", id, ex);
            throw ex;
        }
    }

    @Override
    public UsuarioResponseDTO buscarPorEmail(String email) {
        log.info("Inicio buscarPorEmail - email={}", email);

        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
            UsuarioResponseDTO response = convertirAResponseDTO(usuario);

            log.info("Fin buscarPorEmail - email={}", email);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("Usuario no encontrado por email - email={}", email);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al buscar usuario por email - email={}", email, ex);
            throw ex;
        }
    }

    @Override
    public List<UsuarioResponseDTO> buscarPorRol(String rol) {
        log.info("Inicio buscarPorRol - rol={}", rol);

        try {
            List<UsuarioResponseDTO> usuarios = usuarioRepository.findByRol(rol)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Fin buscarPorRol - rol={}, totalUsuarios={}", rol, usuarios.size());
            return usuarios;
        } catch (RuntimeException ex) {
            log.error("Error al buscar usuarios por rol - rol={}", rol, ex);
            throw ex;
        }
    }

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        log.info("Inicio registrarUsuario - email={}, rol={}", dto.getEmail(), dto.getRol());

        try {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                log.warn("Registro rechazado: email ya registrado - email={}", dto.getEmail());
                throw new AuthBusinessException("El email ya esta registrado");
            }

            Usuario usuario = Usuario.builder()
                    .nombre(dto.getNombre())
                    .email(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .telefono(dto.getTelefono())
                    .rol(dto.getRol())
                    .build();

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            UsuarioResponseDTO response = convertirAResponseDTO(usuarioGuardado);

            log.info("Fin registrarUsuario - id={}, email={}", usuarioGuardado.getId(), usuarioGuardado.getEmail());
            return response;
        } catch (AuthBusinessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al registrar usuario - email={}", dto.getEmail(), ex);
            throw ex;
        }
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Inicio actualizarUsuario - id={}, email={}", id, dto.getEmail());

        try {
            Usuario usuario = obtenerUsuarioPorId(id);

            usuarioRepository.findByEmail(dto.getEmail())
                    .filter(usuarioExistente -> !usuarioExistente.getId().equals(id))
                    .ifPresent(usuarioExistente -> {
                        log.warn("Actualizacion rechazada: email ya registrado - email={}", dto.getEmail());
                        throw new AuthBusinessException("El email ya esta registrado");
                    });

            usuario.setNombre(dto.getNombre());
            usuario.setEmail(dto.getEmail());
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario.setTelefono(dto.getTelefono());
            usuario.setRol(dto.getRol());

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            UsuarioResponseDTO response = convertirAResponseDTO(usuarioActualizado);

            log.info("Fin actualizarUsuario - id={}, email={}", usuarioActualizado.getId(), usuarioActualizado.getEmail());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("Actualizacion fallida: usuario no encontrado - id={}", id);
            throw ex;
        } catch (AuthBusinessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al actualizar usuario - id={}", id, ex);
            throw ex;
        }
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Inicio login - email={}", dto.getEmail());

        try {
            Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Email no registrado"));

            boolean passwordCorrecta = passwordEncoder.matches(dto.getPassword(), usuario.getPassword());

            if (!passwordCorrecta) {
                log.warn("Login rechazado: password incorrecta - email={}", dto.getEmail());
                throw new AuthBusinessException("Password incorrecta");
            }

            String tokenSimple = "TOKEN-" + usuario.getId() + "-" + usuario.getRol();

            LoginResponseDTO response = new LoginResponseDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getRol(),
                    tokenSimple
            );

            log.info("Fin login - id={}, email={}, rol={}", usuario.getId(), usuario.getEmail(), usuario.getRol());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("Login rechazado: email no registrado - email={}", dto.getEmail());
            throw ex;
        } catch (AuthBusinessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al ejecutar login - email={}", dto.getEmail(), ex);
            throw ex;
        }
    }

    @Override
    public String recuperarPassword(RecuperarPasswordDTO dto) {
        log.info("Inicio recuperarPassword - email={}", dto.getEmail());

        try {
            Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Email no registrado"));

            usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
            usuarioRepository.save(usuario);

            log.info("Fin recuperarPassword - id={}, email={}", usuario.getId(), usuario.getEmail());
            return "Password actualizada correctamente";
        } catch (ResourceNotFoundException ex) {
            log.warn("Recuperacion de password fallida: email no registrado - email={}", dto.getEmail());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al recuperar password - email={}", dto.getEmail(), ex);
            throw ex;
        }
    }

    @Override
    public void eliminarUsuario(Long id) {
        log.info("Inicio eliminarUsuario - id={}", id);

        try {
            Usuario usuario = obtenerUsuarioPorId(id);
            usuarioRepository.delete(usuario);

            log.info("Fin eliminarUsuario - id={}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("Eliminacion fallida: usuario no encontrado - id={}", id);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error al eliminar usuario - id={}", id, ex);
            throw ex;
        }
    }

    private Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .build();
    }
}
