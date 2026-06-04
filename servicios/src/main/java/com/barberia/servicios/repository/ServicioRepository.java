package com.barberia.servicios.repository;

import com.barberia.servicios.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByNombreContainingIgnoreCase(String nombre);
}
