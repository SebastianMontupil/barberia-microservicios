package com.barberia.barberos.service;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.dto.UsuarioDTO;
import com.barberia.barberos.model.Barbero;
import com.barberia.barberos.repository.BarberoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BarberoService {

    private final BarberoRepository barberoRepository;
    private final RestTemplate restTemplate;

    public BarberoService(BarberoRepository barberoRepository, RestTemplate restTemplate) {
        this.barberoRepository = barberoRepository;
        this.restTemplate = restTemplate;
    }

    public List<BarberoResponseDTO> listarBarberos() {
        return barberoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<BarberoResponseDTO> buscarPorId(Long id) {
        return barberoRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public Optional<BarberoResponseDTO> buscarPorUsuarioId(Long usuarioId) {
        return barberoRepository.findByUsuarioId(usuarioId)
                .map(this::convertirAResponseDTO);
    }

    public List<BarberoResponseDTO> buscarPorDisponible(Boolean disponible) {
        return barberoRepository.findByDisponible(disponible)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BarberoResponseDTO> buscarPorEspecialidad(String especialidad) {
        return barberoRepository.findByEspecialidad(especialidad)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public BarberoResponseDTO guardarBarbero(BarberoRequestDTO dto) {

        Barbero barbero = new Barbero();

        barbero.setUsuarioId(dto.getUsuarioId());
        barbero.setEspecialidad(dto.getEspecialidad());
        barbero.setHorario(dto.getHorario());
        barbero.setAniosExperiencia(dto.getAniosExperiencia());
        barbero.setDisponible(dto.getDisponible());

        Barbero barberoGuardado = barberoRepository.save(barbero);

        return convertirAResponseDTO(barberoGuardado);
    }

    public BarberoResponseDTO modificarBarbero(Long id, BarberoRequestDTO dto) {

        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        barbero.setUsuarioId(dto.getUsuarioId());
        barbero.setEspecialidad(dto.getEspecialidad());
        barbero.setHorario(dto.getHorario());
        barbero.setAniosExperiencia(dto.getAniosExperiencia());
        barbero.setDisponible(dto.getDisponible());

        Barbero barberoActualizado = barberoRepository.save(barbero);

        return convertirAResponseDTO(barberoActualizado);
    }

    public void eliminarBarbero(Long id) {
        barberoRepository.deleteById(id);
    }

    private BarberoResponseDTO convertirAResponseDTO(Barbero barbero) {

        UsuarioDTO usuario = restTemplate.getForObject(
                "http://localhost:8081/api/usuarios/" + barbero.getUsuarioId(),
                UsuarioDTO.class
        );

        BarberoResponseDTO dto = new BarberoResponseDTO();

        dto.setId(barbero.getId());
        dto.setUsuarioId(barbero.getUsuarioId());

        if (usuario != null) {
            dto.setNombre(usuario.getNombre());
            dto.setEmail(usuario.getEmail());
            dto.setTelefono(usuario.getTelefono());
        }

        dto.setEspecialidad(barbero.getEspecialidad());
        dto.setHorario(barbero.getHorario());
        dto.setAniosExperiencia(barbero.getAniosExperiencia());
        dto.setDisponible(barbero.getDisponible());

        return dto;
    }
}