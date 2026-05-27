package com.barberia.agendas.repository;

import com.barberia.agendas.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByClienteId(Long clienteId);

    List<Agenda> findByBarberoId(Long barberoId);

    List<Agenda> findByFecha(LocalDate fecha);

    List<Agenda> findByEstado(String estado);

    boolean existsByBarberoIdAndFechaAndHoraAndEstadoNot(
            Long barberoId,
            LocalDate fecha,
            java.time.LocalTime hora,
            String estado
    );

    boolean existsByBarberoIdAndFechaAndHoraAndEstadoNotAndIdNot(
            Long barberoId,
            LocalDate fecha,
            java.time.LocalTime hora,
            String estado,
            Long id
    );
}
