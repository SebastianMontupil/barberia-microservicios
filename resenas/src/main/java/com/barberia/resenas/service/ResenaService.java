package com.barberia.resenas.service;

import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;

import java.util.List;

public interface ResenaService {

    List<ResenaResponseDTO> listarResenas();

    ResenaResponseDTO buscarPorId(Long id);

    List<ResenaResponseDTO> buscarPorClienteId(Long clienteId);

    List<ResenaResponseDTO> buscarPorBarberoId(Long barberoId);

    List<ResenaResponseDTO> buscarPorCalificacion(Integer calificacion);

    ResenaResponseDTO guardarResena(ResenaRequestDTO dto);

    ResenaResponseDTO modificarResena(Long id, ResenaRequestDTO dto);

    void eliminarResena(Long id);
}
