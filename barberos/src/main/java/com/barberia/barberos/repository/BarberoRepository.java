package com.barberia.barberos.repository;

import com.barberia.barberos.model.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BarberoRepository extends JpaRepository<Barbero, Long> {

    Optional<Barbero> findByUsuarioId(Long usuarioId);

    List<Barbero> findByDisponible(Boolean disponible);

    List<Barbero> findByEspecialidad(String especialidad);
}