package com.barberia.pagos.service;

import com.barberia.pagos.dto.AgendaDTO;
import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;
import com.barberia.pagos.dto.UsuarioDTO;
import com.barberia.pagos.model.Pago;
import com.barberia.pagos.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    public PagoService(PagoRepository pagoRepository, RestTemplate restTemplate) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = restTemplate;
    }

    public List<PagoResponseDTO> listarPagos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PagoResponseDTO> buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public List<PagoResponseDTO> buscarPorClienteId(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> buscarPorAgendaId(Long agendaId) {
        return pagoRepository.findByAgendaId(agendaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> buscarPorEstadoPago(String estadoPago) {
        return pagoRepository.findByEstadoPago(estadoPago)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> buscarPorMetodoPago(String metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO guardarPago(PagoRequestDTO dto) {

        Pago pago = new Pago();

        pago.setClienteId(dto.getClienteId());
        pago.setAgendaId(dto.getAgendaId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());

        if (dto.getEstadoPago() == null || dto.getEstadoPago().isBlank()) {
            pago.setEstadoPago("PAGADO");
        } else {
            pago.setEstadoPago(dto.getEstadoPago());
        }

        if (dto.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        } else {
            pago.setFechaPago(dto.getFechaPago());
        }

        Pago pagoGuardado = pagoRepository.save(pago);

        return convertirAResponseDTO(pagoGuardado);
    }

    public PagoResponseDTO modificarEstadoPago(Long id, String estadoPago) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstadoPago(estadoPago);

        Pago pagoActualizado = pagoRepository.save(pago);

        return convertirAResponseDTO(pagoActualizado);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    private PagoResponseDTO convertirAResponseDTO(Pago pago) {

        PagoResponseDTO dto = new PagoResponseDTO();

        dto.setId(pago.getId());
        dto.setClienteId(pago.getClienteId());
        dto.setAgendaId(pago.getAgendaId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setFechaPago(pago.getFechaPago());

        try {
            UsuarioDTO cliente = restTemplate.getForObject(
                    "http://localhost:8081/api/usuarios/" + pago.getClienteId(),
                    UsuarioDTO.class
            );

            if (cliente != null) {
                dto.setNombreCliente(cliente.getNombre());
                dto.setEmailCliente(cliente.getEmail());
                dto.setTelefonoCliente(cliente.getTelefono());
            }

        } catch (Exception e) {
            dto.setNombreCliente("Cliente no encontrado");
            dto.setEmailCliente("Sin información");
            dto.setTelefonoCliente("Sin información");
        }

        try {
            AgendaDTO agenda = restTemplate.getForObject(
                    "http://localhost:8083/api/agendas/" + pago.getAgendaId(),
                    AgendaDTO.class
            );

            if (agenda != null) {
                dto.setFechaAgenda(agenda.getFecha());
                dto.setHoraAgenda(agenda.getHora());
                dto.setEstadoAgenda(agenda.getEstado());
                dto.setNombreBarbero(agenda.getNombreBarbero());
            }

        } catch (Exception e) {
            dto.setEstadoAgenda("Agenda no encontrada");
            dto.setNombreBarbero("Sin información");
        }

        return dto;
    }
}