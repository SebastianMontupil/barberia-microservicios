package com.barberia.pagos.service;

import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;

import java.util.List;

public interface PagoService {

    List<PagoResponseDTO> listarPagos();

    PagoResponseDTO buscarPorId(Long id);

    List<PagoResponseDTO> buscarPorClienteId(Long clienteId);

    List<PagoResponseDTO> buscarPorAgendaId(Long agendaId);

    List<PagoResponseDTO> buscarPorEstadoPago(String estadoPago);

    List<PagoResponseDTO> buscarPorMetodoPago(String metodoPago);

    PagoResponseDTO guardarPago(PagoRequestDTO dto);

    PagoResponseDTO actualizarPago(Long id, PagoRequestDTO dto);

    PagoResponseDTO modificarEstadoPago(Long id, String estadoPago);

    void eliminarPago(Long id);
}
