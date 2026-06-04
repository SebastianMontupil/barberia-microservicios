package com.barberia.barberos.service;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;

import java.util.List;

public interface BarberoService {

    List<BarberoResponseDTO> listarBarberos();

    BarberoResponseDTO buscarPorId(Long id);

    BarberoResponseDTO buscarPorUsuarioId(Long usuarioId);

    List<BarberoResponseDTO> buscarPorDisponible(Boolean disponible);

    List<BarberoResponseDTO> buscarPorEspecialidad(String especialidad);

    BarberoResponseDTO guardarBarbero(BarberoRequestDTO dto);

    BarberoResponseDTO modificarBarbero(Long id, BarberoRequestDTO dto);

    BarberoResponseDTO actualizarDisponibilidad(Long id, Boolean disponible);

    void eliminarBarbero(Long id);
}
