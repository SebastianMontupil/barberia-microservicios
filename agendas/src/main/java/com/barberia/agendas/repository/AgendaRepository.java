package com.barberia.agendas.repository;

import com.barberia.agendas.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByClienteId(Long clienteId);

    List<Agenda> findByBarberoId(Long barberoId);

    List<Agenda> findByServicioId(Long servicioId);

    List<Agenda> findByFechaHora(LocalDateTime fechaHora);

    List<Agenda> findByEstado(String estado);

    boolean existsByBarberoIdAndFechaHoraAndEstadoNot(
            Long barberoId,
            LocalDateTime fechaHora,
            String estado
    );

    boolean existsByBarberoIdAndFechaHoraAndEstadoNotAndIdNot(
            Long barberoId,
            LocalDateTime fechaHora,
            String estado,
            Long id
    );
}
