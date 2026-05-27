package com.barberia.resenas.service;

import com.barberia.resenas.dto.BarberoDTO;
import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;
import com.barberia.resenas.dto.UsuarioDTO;
import com.barberia.resenas.model.Resena;
import com.barberia.resenas.repository.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final RestTemplate restTemplate;

    public ResenaService(ResenaRepository resenaRepository, RestTemplate restTemplate) {
        this.resenaRepository = resenaRepository;
        this.restTemplate = restTemplate;
    }

    public List<ResenaResponseDTO> listarResenas() {
        return resenaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResenaResponseDTO> buscarPorId(Long id) {
        return resenaRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public List<ResenaResponseDTO> buscarPorClienteId(Long clienteId) {
        return resenaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResenaResponseDTO> buscarPorBarberoId(Long barberoId) {
        return resenaRepository.findByBarberoId(barberoId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResenaResponseDTO> buscarPorCalificacion(Integer calificacion) {
        return resenaRepository.findByCalificacion(calificacion)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public ResenaResponseDTO guardarResena(ResenaRequestDTO dto) {

        Resena resena = new Resena();

        resena.setClienteId(dto.getClienteId());
        resena.setBarberoId(dto.getBarberoId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setFechaResena(LocalDate.now());

        Resena resenaGuardada = resenaRepository.save(resena);

        return convertirAResponseDTO(resenaGuardada);
    }

    public ResenaResponseDTO modificarResena(Long id, ResenaRequestDTO dto) {

        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        resena.setClienteId(dto.getClienteId());
        resena.setBarberoId(dto.getBarberoId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());

        Resena resenaActualizada = resenaRepository.save(resena);

        return convertirAResponseDTO(resenaActualizada);
    }

    public void eliminarResena(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada");
        }

        resenaRepository.deleteById(id);
    }

    private ResenaResponseDTO convertirAResponseDTO(Resena resena) {

        ResenaResponseDTO dto = new ResenaResponseDTO();

        dto.setId(resena.getId());
        dto.setClienteId(resena.getClienteId());
        dto.setBarberoId(resena.getBarberoId());
        dto.setCalificacion(resena.getCalificacion());
        dto.setComentario(resena.getComentario());
        dto.setFechaResena(resena.getFechaResena());

        try {
            UsuarioDTO cliente = restTemplate.getForObject(
                    "http://localhost:8081/api/usuarios/" + resena.getClienteId(),
                    UsuarioDTO.class
            );

            if (cliente != null) {
                dto.setNombreCliente(cliente.getNombre());
                dto.setEmailCliente(cliente.getEmail());
            }

        } catch (Exception e) {
            dto.setNombreCliente("Cliente no encontrado");
            dto.setEmailCliente("Sin información");
        }

        try {
            BarberoDTO barbero = restTemplate.getForObject(
                    "http://localhost:8082/api/barberos/" + resena.getBarberoId(),
                    BarberoDTO.class
            );

            if (barbero != null) {
                dto.setNombreBarbero(barbero.getNombre());
                dto.setEspecialidadBarbero(barbero.getEspecialidad());
            }

        } catch (Exception e) {
            dto.setNombreBarbero("Barbero no encontrado");
            dto.setEspecialidadBarbero("Sin información");
        }

        return dto;
    }
}
