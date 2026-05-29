package com.barberia.pagos.service;

import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;
import com.barberia.pagos.exception.ResourceNotFoundException;
import com.barberia.pagos.model.Pago;
import com.barberia.pagos.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public List<PagoResponseDTO> listarPagos() {
        log.info("Iniciando listado de pagos");

        try {
            List<PagoResponseDTO> pagos = pagoRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Listado de pagos finalizado. Total encontrados: {}", pagos.size());
            return pagos;
        } catch (DataAccessException ex) {
            log.error("Fallo al listar pagos", ex);
            throw ex;
        }
    }

    @Override
    public PagoResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de pago por id: {}", id);

        try {
            Pago pago = obtenerPagoPorId(id);
            PagoResponseDTO response = convertirAResponseDTO(pago);

            log.info("Busqueda de pago por id finalizada. id: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro pago por id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Fallo al buscar pago por id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public List<PagoResponseDTO> buscarPorClienteId(Long clienteId) {
        log.info("Iniciando busqueda de pagos por clienteId: {}", clienteId);

        try {
            List<PagoResponseDTO> pagos = pagoRepository.findByClienteId(clienteId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por clienteId finalizada. clienteId: {}, total encontrados: {}", clienteId, pagos.size());
            return pagos;
        } catch (DataAccessException ex) {
            log.error("Fallo al buscar pagos por clienteId: {}", clienteId, ex);
            throw ex;
        }
    }

    @Override
    public List<PagoResponseDTO> buscarPorAgendaId(Long agendaId) {
        log.info("Iniciando busqueda de pagos por agendaId: {}", agendaId);

        try {
            List<PagoResponseDTO> pagos = pagoRepository.findByAgendaId(agendaId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por agendaId finalizada. agendaId: {}, total encontrados: {}", agendaId, pagos.size());
            return pagos;
        } catch (DataAccessException ex) {
            log.error("Fallo al buscar pagos por agendaId: {}", agendaId, ex);
            throw ex;
        }
    }

    @Override
    public List<PagoResponseDTO> buscarPorEstadoPago(String estadoPago) {
        log.info("Iniciando busqueda de pagos por estadoPago: {}", estadoPago);

        try {
            List<PagoResponseDTO> pagos = pagoRepository.findByEstadoPago(estadoPago)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por estadoPago finalizada. estadoPago: {}, total encontrados: {}", estadoPago, pagos.size());
            return pagos;
        } catch (DataAccessException ex) {
            log.error("Fallo al buscar pagos por estadoPago: {}", estadoPago, ex);
            throw ex;
        }
    }

    @Override
    public List<PagoResponseDTO> buscarPorMetodoPago(String metodoPago) {
        log.info("Iniciando busqueda de pagos por metodoPago: {}", metodoPago);

        try {
            List<PagoResponseDTO> pagos = pagoRepository.findByMetodoPago(metodoPago)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por metodoPago finalizada. metodoPago: {}, total encontrados: {}", metodoPago, pagos.size());
            return pagos;
        } catch (DataAccessException ex) {
            log.error("Fallo al buscar pagos por metodoPago: {}", metodoPago, ex);
            throw ex;
        }
    }

    @Override
    public PagoResponseDTO guardarPago(PagoRequestDTO dto) {
        log.info("Iniciando registro de pago para agendaId: {}, clienteId: {}", dto.getAgendaId(), dto.getClienteId());

        try {
            Pago pago = Pago.builder()
                    .agendaId(dto.getAgendaId())
                    .clienteId(dto.getClienteId())
                    .monto(dto.getMonto())
                    .fechaPago(dto.getFechaPago())
                    .estadoPago(dto.getEstadoPago())
                    .metodoPago(dto.getMetodoPago())
                    .build();

            Pago pagoGuardado = pagoRepository.save(pago);
            PagoResponseDTO response = convertirAResponseDTO(pagoGuardado);

            log.info("Registro de pago finalizado. id: {}", pagoGuardado.getId());
            return response;
        } catch (DataAccessException ex) {
            log.error("Fallo al registrar pago para agendaId: {}, clienteId: {}", dto.getAgendaId(), dto.getClienteId(), ex);
            throw ex;
        }
    }

    @Override
    public PagoResponseDTO actualizarPago(Long id, PagoRequestDTO dto) {
        log.info("Iniciando actualizacion de pago. id: {}", id);

        try {
            Pago pago = obtenerPagoPorId(id);
            pago.setAgendaId(dto.getAgendaId());
            pago.setClienteId(dto.getClienteId());
            pago.setMonto(dto.getMonto());
            pago.setFechaPago(dto.getFechaPago());
            pago.setEstadoPago(dto.getEstadoPago());
            pago.setMetodoPago(dto.getMetodoPago());

            Pago pagoActualizado = pagoRepository.save(pago);
            PagoResponseDTO response = convertirAResponseDTO(pagoActualizado);

            log.info("Actualizacion de pago finalizada. id: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro pago para actualizar. id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Fallo al actualizar pago. id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public PagoResponseDTO modificarEstadoPago(Long id, String estadoPago) {
        log.info("Iniciando modificacion de estadoPago. id: {}, estadoPago: {}", id, estadoPago);

        try {
            Pago pago = obtenerPagoPorId(id);
            pago.setEstadoPago(estadoPago);

            Pago pagoActualizado = pagoRepository.save(pago);
            PagoResponseDTO response = convertirAResponseDTO(pagoActualizado);

            log.info("Modificacion de estadoPago finalizada. id: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro pago para modificar estadoPago. id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Fallo al modificar estadoPago. id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public void eliminarPago(Long id) {
        log.info("Iniciando eliminacion de pago. id: {}", id);

        try {
            Pago pago = obtenerPagoPorId(id);
            pagoRepository.delete(pago);

            log.info("Eliminacion de pago finalizada. id: {}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro pago para eliminar. id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Fallo al eliminar pago. id: {}", id, ex);
            throw ex;
        }
    }

    private Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    private PagoResponseDTO convertirAResponseDTO(Pago pago) {
        return PagoResponseDTO.builder()
                .id(pago.getId())
                .agendaId(pago.getAgendaId())
                .clienteId(pago.getClienteId())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .estadoPago(pago.getEstadoPago())
                .metodoPago(pago.getMetodoPago())
                .build();
    }
}
