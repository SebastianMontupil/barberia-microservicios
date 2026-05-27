package com.barberia.servicios.service;

import com.barberia.servicios.model.Servicio;
import com.barberia.servicios.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> buscarPorDisponible(Boolean disponible) {
        return servicioRepository.findByDisponible(disponible);
    }

    public List<Servicio> buscarPorNombre(String nombre) {
        return servicioRepository.findByNombreContaining(nombre);
    }

    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio modificarServicio(Long id, Servicio servicioNuevo) {

        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setNombre(servicioNuevo.getNombre());
        servicio.setDescripcion(servicioNuevo.getDescripcion());
        servicio.setDuracionMinutos(servicioNuevo.getDuracionMinutos());
        servicio.setPrecio(servicioNuevo.getPrecio());
        servicio.setDisponible(servicioNuevo.getDisponible());

        return servicioRepository.save(servicio);
    }

    public void eliminarServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado");
        }

        servicioRepository.deleteById(id);
    }
}
