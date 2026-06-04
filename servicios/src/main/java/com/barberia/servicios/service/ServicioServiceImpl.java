package com.barberia.servicios.service;

import com.barberia.servicios.dto.ServicioRequestDTO;
import com.barberia.servicios.dto.ServicioResponseDTO;
import com.barberia.servicios.exception.ResourceNotFoundException;
import com.barberia.servicios.model.Servicio;
import com.barberia.servicios.repository.ServicioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarServicios() {
        log.info("Iniciando listado de servicios");

        try {
            List<ServicioResponseDTO> servicios = servicioRepository.findAll()
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Listado de servicios finalizado. Total: {}", servicios.size());
            return servicios;
        } catch (DataAccessException ex) {
            log.error("Error al listar servicios", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO buscarPorId(Long id) {
        log.info("Iniciando busqueda de servicio por id: {}", id);

        try {
            Servicio servicio = obtenerServicioPorId(id);
            ServicioResponseDTO response = convertirAResponseDTO(servicio);

            log.info("Busqueda de servicio por id finalizada: {}", id);
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se encontro servicio con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al buscar servicio por id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> buscarPorNombre(String nombre) {
        log.info("Iniciando busqueda de servicios por nombre: {}", nombre);

        try {
            List<ServicioResponseDTO> servicios = servicioRepository.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .map(this::convertirAResponseDTO)
                    .toList();

            log.info("Busqueda de servicios por nombre finalizada. nombre={}, total={}", nombre, servicios.size());
            return servicios;
        } catch (DataAccessException ex) {
            log.error("Error al buscar servicios por nombre: {}", nombre, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ServicioResponseDTO guardarServicio(ServicioRequestDTO dto) {
        log.info("Iniciando creacion de servicio: {}", dto.getNombre());

        try {
            Servicio servicio = convertirAEntidad(dto);
            Servicio servicioGuardado = servicioRepository.save(servicio);
            ServicioResponseDTO response = convertirAResponseDTO(servicioGuardado);

            log.info("Creacion de servicio finalizada. id={}, nombre={}", servicioGuardado.getId(), servicioGuardado.getNombre());
            return response;
        } catch (DataAccessException ex) {
            log.error("Error al crear servicio: {}", dto.getNombre(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public ServicioResponseDTO modificarServicio(Long id, ServicioRequestDTO dto) {
        log.info("Iniciando actualizacion de servicio id: {}", id);

        try {
            Servicio servicio = obtenerServicioPorId(id);

            servicio.setNombre(dto.getNombre());
            servicio.setDescripcion(dto.getDescripcion());
            servicio.setPrecio(dto.getPrecio());
            servicio.setDuracion(dto.getDuracion());

            Servicio servicioActualizado = servicioRepository.save(servicio);
            ServicioResponseDTO response = convertirAResponseDTO(servicioActualizado);

            log.info("Actualizacion de servicio finalizada. id={}, nombre={}", id, servicioActualizado.getNombre());
            return response;
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo actualizar. Servicio no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al actualizar servicio id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void eliminarServicio(Long id) {
        log.info("Iniciando eliminacion de servicio id: {}", id);

        try {
            Servicio servicio = obtenerServicioPorId(id);
            servicioRepository.delete(servicio);

            log.info("Eliminacion de servicio finalizada. id={}", id);
        } catch (ResourceNotFoundException ex) {
            log.warn("No se pudo eliminar. Servicio no encontrado con id: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Error al eliminar servicio id: {}", id, ex);
            throw ex;
        }
    }

    private Servicio obtenerServicioPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
    }

    private Servicio convertirAEntidad(ServicioRequestDTO dto) {
        return Servicio.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .duracion(dto.getDuracion())
                .build();
    }

    private ServicioResponseDTO convertirAResponseDTO(Servicio servicio) {
        return ServicioResponseDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precio(servicio.getPrecio())
                .duracion(servicio.getDuracion())
                .build();
    }
}
