package com.barberia.agendas.service;

import com.barberia.agendas.dto.AgendaRequestDTO;
import com.barberia.agendas.dto.AgendaResponseDTO;
import com.barberia.agendas.exception.AgendaBusinessException;
import com.barberia.agendas.exception.ResourceNotFoundException;
import com.barberia.agendas.model.Agenda;
import com.barberia.agendas.repository.AgendaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgendaServiceImpl implements AgendaService {

    private static final String ESTADO_CANCELADA = "CANCELADA";
    private static final String ESTADO_REPROGRAMADA = "REPROGRAMADA";

    private final AgendaRepository agendaRepository;

    public AgendaServiceImpl(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public List<AgendaResponseDTO> listarAgendas() {
        log.info("Iniciando listado de agendas");
        try {
            List<AgendaResponseDTO> agendas = agendaRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
            log.info("Listado de agendas finalizado. Total: {}", agendas.size());
            return agendas;
        } catch (DataAccessException ex) {
            log.error("Error al listar agendas", ex);
            throw new AgendaBusinessException("No fue posible listar las agendas");
        }
    }

    @Override
    public AgendaResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de agenda por id: {}", id);
        Agenda agenda = buscarAgendaPorId(id);
        log.info("Busqueda de agenda por id finalizada. id: {}", id);
        return convertirAResponseDTO(agenda);
    }

    @Override
    public List<AgendaResponseDTO> buscarPorClienteId(Long clienteId) {
        log.info("Iniciando busqueda de agendas por clienteId: {}", clienteId);
        try {
            List<AgendaResponseDTO> agendas = agendaRepository.findByClienteId(clienteId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
            log.info("Busqueda por clienteId finalizada. clienteId: {}, total: {}", clienteId, agendas.size());
            return agendas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar agendas por clienteId: {}", clienteId, ex);
            throw new AgendaBusinessException("No fue posible buscar agendas por cliente");
        }
    }

    @Override
    public List<AgendaResponseDTO> buscarPorBarberoId(Long barberoId) {
        log.info("Iniciando busqueda de agendas por barberoId: {}", barberoId);
        try {
            List<AgendaResponseDTO> agendas = agendaRepository.findByBarberoId(barberoId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
            log.info("Busqueda por barberoId finalizada. barberoId: {}, total: {}", barberoId, agendas.size());
            return agendas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar agendas por barberoId: {}", barberoId, ex);
            throw new AgendaBusinessException("No fue posible buscar agendas por barbero");
        }
    }

    @Override
    public List<AgendaResponseDTO> buscarPorServicioId(Long servicioId) {
        log.info("Iniciando busqueda de agendas por servicioId: {}", servicioId);
        try {
            List<AgendaResponseDTO> agendas = agendaRepository.findByServicioId(servicioId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
            log.info("Busqueda por servicioId finalizada. servicioId: {}, total: {}", servicioId, agendas.size());
            return agendas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar agendas por servicioId: {}", servicioId, ex);
            throw new AgendaBusinessException("No fue posible buscar agendas por servicio");
        }
    }

    @Override
    public List<AgendaResponseDTO> buscarPorEstado(String estado) {
        log.info("Iniciando busqueda de agendas por estado: {}", estado);
        try {
            List<AgendaResponseDTO> agendas = agendaRepository.findByEstado(estado)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
            log.info("Busqueda por estado finalizada. estado: {}, total: {}", estado, agendas.size());
            return agendas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar agendas por estado: {}", estado, ex);
            throw new AgendaBusinessException("No fue posible buscar agendas por estado");
        }
    }

    @Override
    public AgendaResponseDTO guardarAgenda(AgendaRequestDTO dto) {
        log.info("Iniciando creacion de agenda. clienteId: {}, barberoId: {}, servicioId: {}, fechaHora: {}",
                dto.getClienteId(), dto.getBarberoId(), dto.getServicioId(), dto.getFechaHora());
        try {
            validarDisponibilidadBarbero(dto);

            Agenda agenda = Agenda.builder()
                    .clienteId(dto.getClienteId())
                    .barberoId(dto.getBarberoId())
                    .servicioId(dto.getServicioId())
                    .fechaHora(dto.getFechaHora())
                    .estado(dto.getEstado())
                    .build();

            Agenda agendaGuardada = agendaRepository.save(agenda);
            log.info("Agenda creada correctamente. id: {}", agendaGuardada.getId());
            return convertirAResponseDTO(agendaGuardada);
        } catch (AgendaBusinessException ex) {
            log.warn("Falla de negocio al crear agenda: {}", ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al crear agenda", ex);
            throw new AgendaBusinessException("No fue posible crear la agenda");
        }
    }

    @Override
    public AgendaResponseDTO actualizarAgenda(Long id, AgendaRequestDTO dto) {
        log.info("Iniciando actualizacion de agenda. id: {}", id);
        try {
            Agenda agenda = buscarAgendaPorId(id);
            validarDisponibilidadBarberoExcluyendoAgenda(id, dto);

            agenda.setClienteId(dto.getClienteId());
            agenda.setBarberoId(dto.getBarberoId());
            agenda.setServicioId(dto.getServicioId());
            agenda.setFechaHora(dto.getFechaHora());
            agenda.setEstado(dto.getEstado());

            Agenda agendaActualizada = agendaRepository.save(agenda);
            log.info("Agenda actualizada correctamente. id: {}", id);
            return convertirAResponseDTO(agendaActualizada);
        } catch (ResourceNotFoundException | AgendaBusinessException ex) {
            log.warn("Falla al actualizar agenda. id: {}, motivo: {}", id, ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al actualizar agenda. id: {}", id, ex);
            throw new AgendaBusinessException("No fue posible actualizar la agenda");
        }
    }

    @Override
    public AgendaResponseDTO cancelarAgenda(Long id) {
        log.info("Iniciando cancelacion de agenda. id: {}", id);
        try {
            Agenda agenda = buscarAgendaPorId(id);
            agenda.setEstado(ESTADO_CANCELADA);

            Agenda agendaActualizada = agendaRepository.save(agenda);
            log.info("Agenda cancelada correctamente. id: {}", id);
            return convertirAResponseDTO(agendaActualizada);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo cancelar agenda. id: {}, motivo: {}", id, ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al cancelar agenda. id: {}", id, ex);
            throw new AgendaBusinessException("No fue posible cancelar la agenda");
        }
    }

    @Override
    public AgendaResponseDTO reprogramarAgenda(Long id, AgendaRequestDTO dto) {
        log.info("Iniciando reprogramacion de agenda. id: {}, nueva fechaHora: {}", id, dto.getFechaHora());
        try {
            Agenda agenda = buscarAgendaPorId(id);
            validarDisponibilidadBarberoExcluyendoAgenda(id, dto);

            agenda.setClienteId(dto.getClienteId());
            agenda.setBarberoId(dto.getBarberoId());
            agenda.setServicioId(dto.getServicioId());
            agenda.setFechaHora(dto.getFechaHora());
            agenda.setEstado(ESTADO_REPROGRAMADA);

            Agenda agendaActualizada = agendaRepository.save(agenda);
            log.info("Agenda reprogramada correctamente. id: {}", id);
            return convertirAResponseDTO(agendaActualizada);
        } catch (ResourceNotFoundException | AgendaBusinessException ex) {
            log.warn("Falla al reprogramar agenda. id: {}, motivo: {}", id, ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al reprogramar agenda. id: {}", id, ex);
            throw new AgendaBusinessException("No fue posible reprogramar la agenda");
        }
    }

    @Override
    public void eliminarAgenda(Long id) {
        log.info("Iniciando eliminacion de agenda. id: {}", id);
        try {
            Agenda agenda = buscarAgendaPorId(id);
            agendaRepository.delete(agenda);
            log.info("Agenda eliminada correctamente. id: {}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo eliminar agenda. id: {}, motivo: {}", id, ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al eliminar agenda. id: {}", id, ex);
            throw new AgendaBusinessException("No fue posible eliminar la agenda");
        }
    }

    private Agenda buscarAgendaPorId(Long id) {
        try {
            return agendaRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Agenda no encontrada. id: {}", id);
                        return new ResourceNotFoundException("Agenda no encontrada con id: " + id);
                    });
        } catch (DataAccessException ex) {
            log.error("Error de persistencia al buscar agenda por id: {}", id, ex);
            throw new AgendaBusinessException("No fue posible buscar la agenda");
        }
    }

    private void validarDisponibilidadBarbero(AgendaRequestDTO dto) {
        boolean ocupado = agendaRepository.existsByBarberoIdAndFechaHoraAndEstadoNot(
                dto.getBarberoId(),
                dto.getFechaHora(),
                ESTADO_CANCELADA
        );

        if (ocupado) {
            log.warn("Barbero ocupado. barberoId: {}, fechaHora: {}", dto.getBarberoId(), dto.getFechaHora());
            throw new AgendaBusinessException("El barbero ya tiene una reserva en esa fecha y hora");
        }
    }

    private void validarDisponibilidadBarberoExcluyendoAgenda(Long id, AgendaRequestDTO dto) {
        boolean ocupado = agendaRepository.existsByBarberoIdAndFechaHoraAndEstadoNotAndIdNot(
                dto.getBarberoId(),
                dto.getFechaHora(),
                ESTADO_CANCELADA,
                id
        );

        if (ocupado) {
            log.warn("Barbero ocupado al modificar agenda. id: {}, barberoId: {}, fechaHora: {}",
                    id, dto.getBarberoId(), dto.getFechaHora());
            throw new AgendaBusinessException("El barbero ya tiene una reserva en esa fecha y hora");
        }
    }

    private AgendaResponseDTO convertirAResponseDTO(Agenda agenda) {
        return AgendaResponseDTO.builder()
                .id(agenda.getId())
                .clienteId(agenda.getClienteId())
                .barberoId(agenda.getBarberoId())
                .servicioId(agenda.getServicioId())
                .fechaHora(agenda.getFechaHora())
                .estado(agenda.getEstado())
                .build();
    }
}
