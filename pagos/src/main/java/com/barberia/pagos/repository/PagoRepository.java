package com.barberia.pagos.repository;

import com.barberia.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByClienteId(Long clienteId);

    List<Pago> findByAgendaId(Long agendaId);

    List<Pago> findByEstadoPago(String estadoPago);

    List<Pago> findByMetodoPago(String metodoPago);
}
