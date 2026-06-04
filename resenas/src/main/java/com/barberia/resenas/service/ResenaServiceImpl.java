package com.barberia.resenas.service;

import com.barberia.resenas.dto.ResenaRequestDTO;
import com.barberia.resenas.dto.ResenaResponseDTO;
import com.barberia.resenas.exception.ResourceNotFoundException;
import com.barberia.resenas.model.Resena;
import com.barberia.resenas.repository.ResenaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> listarResenas() {
        log.info("Iniciando listado de resenas");

        try {
            List<ResenaResponseDTO> resenas = resenaRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Listado de resenas finalizado. Total: {}", resenas.size());
            return resenas;
        } catch (DataAccessException ex) {
            log.error("Error al listar resenas", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResenaResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de resena por id: {}", id);

        try {
            validarIdentificador(id, "id");
            ResenaResponseDTO response = convertirAResponseDTO(obtenerResenaPorId(id));

            log.info("Busqueda de resena por id finalizada. id={}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro resena con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al buscar resena por id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> buscarPorClienteId(Long clienteId) {
        log.info("Iniciando busqueda de resenas por clienteId: {}", clienteId);

        try {
            validarIdentificador(clienteId, "clienteId");
            List<ResenaResponseDTO> resenas = resenaRepository.findByClienteId(clienteId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por clienteId finalizada. clienteId={}, total={}", clienteId, resenas.size());
            return resenas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar resenas por clienteId: {}", clienteId, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> buscarPorBarberoId(Long barberoId) {
        log.info("Iniciando busqueda de resenas por barberoId: {}", barberoId);

        try {
            validarIdentificador(barberoId, "barberoId");
            List<ResenaResponseDTO> resenas = resenaRepository.findByBarberoId(barberoId)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por barberoId finalizada. barberoId={}, total={}", barberoId, resenas.size());
            return resenas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar resenas por barberoId: {}", barberoId, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> buscarPorCalificacion(Integer calificacion) {
        log.info("Iniciando busqueda de resenas por calificacion: {}", calificacion);

        try {
            validarCalificacion(calificacion);
            List<ResenaResponseDTO> resenas = resenaRepository.findByCalificacion(calificacion)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda por calificacion finalizada. calificacion={}, total={}", calificacion, resenas.size());
            return resenas;
        } catch (DataAccessException ex) {
            log.error("Error al buscar resenas por calificacion: {}", calificacion, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResenaResponseDTO guardarResena(ResenaRequestDTO dto) {
        log.info("Iniciando creacion de resena. clienteId={}, barberoId={}", dto.getClienteId(), dto.getBarberoId());

        try {
            Resena resena = convertirAEntidad(dto);
            Resena resenaGuardada = resenaRepository.save(resena);
            ResenaResponseDTO response = convertirAResponseDTO(resenaGuardada);

            log.info("Creacion de resena finalizada. id={}", resenaGuardada.getId());
            return response;
        } catch (DataAccessException ex) {
            log.error("Error al crear resena. clienteId={}, barberoId={}", dto.getClienteId(), dto.getBarberoId(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResenaResponseDTO modificarResena(Long id, ResenaRequestDTO dto) {
        log.info("Iniciando actualizacion de resena id: {}", id);

        try {
            validarIdentificador(id, "id");
            Resena resena = obtenerResenaPorId(id);

            resena.setClienteId(dto.getClienteId());
            resena.setBarberoId(dto.getBarberoId());
            resena.setCalificacion(dto.getCalificacion());
            resena.setComentario(dto.getComentario());
            resena.setFecha(dto.getFecha());

            Resena resenaActualizada = resenaRepository.save(resena);
            ResenaResponseDTO response = convertirAResponseDTO(resenaActualizada);

            log.info("Actualizacion de resena finalizada. id={}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo actualizar. Resena no encontrada con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al actualizar resena id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void eliminarResena(Long id) {
        log.info("Iniciando eliminacion de resena id: {}", id);

        try {
            validarIdentificador(id, "id");
            Resena resena = obtenerResenaPorId(id);
            resenaRepository.delete(resena);

            log.info("Eliminacion de resena finalizada. id={}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo eliminar. Resena no encontrada con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al eliminar resena id: {}", id, ex);
            throw ex;
        }
    }

    private Resena obtenerResenaPorId(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resena no encontrada con id: " + id));
    }

    private void validarIdentificador(Long valor, String campo) {
        if (valor == null || valor <= 0) {
            log.warn("Identificador invalido. campo={}, valor={}", campo, valor);
            throw new IllegalArgumentException("El " + campo + " debe ser mayor a 0");
        }
    }

    private void validarCalificacion(Integer calificacion) {
        if (calificacion == null || calificacion < 1 || calificacion > 5) {
            log.warn("Calificacion invalida: {}", calificacion);
            throw new IllegalArgumentException("La calificacion debe estar entre 1 y 5");
        }
    }

    private Resena convertirAEntidad(ResenaRequestDTO dto) {
        return Resena.builder()
                .clienteId(dto.getClienteId())
                .barberoId(dto.getBarberoId())
                .calificacion(dto.getCalificacion())
                .comentario(dto.getComentario())
                .fecha(dto.getFecha())
                .build();
    }

    private ResenaResponseDTO convertirAResponseDTO(Resena resena) {
        return ResenaResponseDTO.builder()
                .id(resena.getId())
                .clienteId(resena.getClienteId())
                .barberoId(resena.getBarberoId())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .fecha(resena.getFecha())
                .build();
    }
}
