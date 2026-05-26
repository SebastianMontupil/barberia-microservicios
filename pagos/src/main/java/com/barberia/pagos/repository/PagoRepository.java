package com.barberia.pagos.repository;

import com.barberia.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByClienteId(Long clienteId);

    List<Pago> findByAgendaId(Long agendaId);

    List<Pago> findByEstadoPago(String estadoPago);

    List<Pago> findByMetodoPago(String metodoPago);
}