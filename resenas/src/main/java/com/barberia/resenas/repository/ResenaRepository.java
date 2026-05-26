package com.barberia.resenas.repository;

import com.barberia.resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByClienteId(Long clienteId);

    List<Resena> findByBarberoId(Long barberoId);

    List<Resena> findByCalificacion(Integer calificacion);
}