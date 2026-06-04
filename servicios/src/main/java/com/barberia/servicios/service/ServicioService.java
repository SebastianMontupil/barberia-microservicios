package com.barberia.servicios.service;

import com.barberia.servicios.dto.ServicioRequestDTO;
import com.barberia.servicios.dto.ServicioResponseDTO;

import java.util.List;

public interface ServicioService {

    List<ServicioResponseDTO> listarServicios();

    ServicioResponseDTO buscarPorId(Long id);

    List<ServicioResponseDTO> buscarPorNombre(String nombre);

    ServicioResponseDTO guardarServicio(ServicioRequestDTO dto);

    ServicioResponseDTO modificarServicio(Long id, ServicioRequestDTO dto);

    void eliminarServicio(Long id);
}
