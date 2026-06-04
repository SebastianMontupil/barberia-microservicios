package com.barberia.barberos.service;

import com.barberia.barberos.dto.BarberoRequestDTO;
import com.barberia.barberos.dto.BarberoResponseDTO;
import com.barberia.barberos.exception.ResourceNotFoundException;
import com.barberia.barberos.model.Barbero;
import com.barberia.barberos.repository.BarberoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BarberoServiceImpl implements BarberoService {

    private final BarberoRepository barberoRepository;

    public BarberoServiceImpl(BarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    @Override
    public List<BarberoResponseDTO> listarBarberos() {
        log.info("Iniciando listado de barberos");

        try {
            List<BarberoResponseDTO> barberos = barberoRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Listado de barberos finalizado. Total: {}", barberos.size());
            return barberos;
        } catch (DataAccessException ex) {
            log.error("Error al listar barberos", ex);
            throw ex;
        }
    }

    @Override
    public BarberoResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de barbero por id: {}", id);

        try {
            Barbero barbero = obtenerBarberoPorId(id);
            BarberoResponseDTO response = convertirAResponseDTO(barbero);

            log.info("Busqueda de barbero por id finalizada: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro barbero con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al buscar barbero por id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public BarberoResponseDTO buscarPorUsuarioId(Long usuarioId) {
        log.info("Iniciando busqueda de barbero por usuarioId: {}", usuarioId);

        try {
            Barbero barbero = barberoRepository.findByUsuarioId(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Barbero no encontrado para usuarioId: " + usuarioId));
            BarberoResponseDTO response = convertirAResponseDTO(barbero);

            log.info("Busqueda de barbero por usuarioId finalizada: {}", usuarioId);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro barbero para usuarioId: {}", usuarioId);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al buscar barbero por usuarioId: {}", usuarioId, ex);
            throw ex;
        }
    }

    @Override
    public List<BarberoResponseDTO> buscarPorDisponible(Boolean disponible) {
        log.info("Iniciando busqueda de barberos por disponibilidad: {}", disponible);

        try {
            List<BarberoResponseDTO> barberos = barberoRepository.findByDisponible(disponible)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por disponibilidad finalizada. disponible={}, total={}", disponible, barberos.size());
            return barberos;
        } catch (DataAccessException ex) {
            log.error("Error al buscar barberos por disponibilidad: {}", disponible, ex);
            throw ex;
        }
    }

    @Override
    public List<BarberoResponseDTO> buscarPorEspecialidad(String especialidad) {
        log.info("Iniciando busqueda de barberos por especialidad: {}", especialidad);

        try {
            List<BarberoResponseDTO> barberos = barberoRepository.findByEspecialidad(especialidad)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por especialidad finalizada. especialidad={}, total={}", especialidad, barberos.size());
            return barberos;
        } catch (DataAccessException ex) {
            log.error("Error al buscar barberos por especialidad: {}", especialidad, ex);
            throw ex;
        }
    }

    @Override
    public BarberoResponseDTO guardarBarbero(BarberoRequestDTO dto) {
        log.info("Iniciando creacion de barbero para usuarioId: {}", dto.getUsuarioId());

        try {
            Barbero barbero = new Barbero();
            barbero.setUsuarioId(dto.getUsuarioId());
            barbero.setEspecialidad(dto.getEspecialidad());
            barbero.setHorario(dto.getHorario());
            barbero.setAniosExperiencia(dto.getAniosExperiencia());
            barbero.setDisponible(dto.getDisponible());

            Barbero barberoGuardado = barberoRepository.save(barbero);
            BarberoResponseDTO response = convertirAResponseDTO(barberoGuardado);

            log.info("Creacion de barbero finalizada. id={}, usuarioId={}", barberoGuardado.getId(), barberoGuardado.getUsuarioId());
            return response;
        } catch (DataAccessException ex) {
            log.error("Error al crear barbero para usuarioId: {}", dto.getUsuarioId(), ex);
            throw ex;
        }
    }

    @Override
    public BarberoResponseDTO modificarBarbero(Long id, BarberoRequestDTO dto) {
        log.info("Iniciando actualizacion de barbero id: {}", id);

        try {
            Barbero barbero = obtenerBarberoPorId(id);

            barbero.setUsuarioId(dto.getUsuarioId());
            barbero.setEspecialidad(dto.getEspecialidad());
            barbero.setHorario(dto.getHorario());
            barbero.setAniosExperiencia(dto.getAniosExperiencia());
            barbero.setDisponible(dto.getDisponible());

            Barbero barberoActualizado = barberoRepository.save(barbero);
            BarberoResponseDTO response = convertirAResponseDTO(barberoActualizado);

            log.info("Actualizacion de barbero finalizada. id={}, usuarioId={}", id, barberoActualizado.getUsuarioId());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo actualizar. Barbero no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al actualizar barbero id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public BarberoResponseDTO actualizarDisponibilidad(Long id, Boolean disponible) {
        log.info("Iniciando actualizacion de disponibilidad para barbero id: {}", id);

        try {
            Barbero barbero = obtenerBarberoPorId(id);
            barbero.setDisponible(disponible);

            Barbero barberoActualizado = barberoRepository.save(barbero);
            BarberoResponseDTO response = convertirAResponseDTO(barberoActualizado);

            log.info("Disponibilidad actualizada para barbero id: {}. disponible={}", id, disponible);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo actualizar disponibilidad. Barbero no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al actualizar disponibilidad para barbero id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public void eliminarBarbero(Long id) {
        log.info("Iniciando eliminacion de barbero id: {}", id);

        try {
            Barbero barbero = obtenerBarberoPorId(id);
            barberoRepository.delete(barbero);

            log.info("Eliminacion de barbero finalizada. id={}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo eliminar. Barbero no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al eliminar barbero id: {}", id, ex);
            throw ex;
        }
    }

    private Barbero obtenerBarberoPorId(Long id) {
        return barberoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbero no encontrado con id: " + id));
    }

    private BarberoResponseDTO convertirAResponseDTO(Barbero barbero) {
        return BarberoResponseDTO.builder()
                .id(barbero.getId())
                .usuarioId(barbero.getUsuarioId())
                .especialidad(barbero.getEspecialidad())
                .horario(barbero.getHorario())
                .aniosExperiencia(barbero.getAniosExperiencia())
                .disponible(barbero.getDisponible())
                .build();
    }
}
