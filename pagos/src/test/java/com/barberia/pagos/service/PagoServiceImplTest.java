package com.barberia.pagos.service;

import com.barberia.pagos.dto.PagoRequestDTO;
import com.barberia.pagos.dto.PagoResponseDTO;
import com.barberia.pagos.exception.ResourceNotFoundException;
import com.barberia.pagos.model.Pago;
import com.barberia.pagos.repository.PagoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock private PagoRepository pagoRepository;
    @InjectMocks private PagoServiceImpl pagoService;
    private Pago pago;
    private PagoRequestDTO request;

    @BeforeEach
    void prepararDatos() {
        LocalDateTime fechaPago = LocalDateTime.of(2026, 6, 20, 12, 0);
        pago = Pago.builder().id(1L).agendaId(2L).clienteId(3L).monto(12000.0)
                .fechaPago(fechaPago).estadoPago("PAGADO").metodoPago("EFECTIVO").build();
        request = PagoRequestDTO.builder().agendaId(2L).clienteId(3L).monto(12000.0)
                .fechaPago(fechaPago).estadoPago("PAGADO").metodoPago("EFECTIVO").build();
    }

    @Test
    void listarPagos_deberiaRetornarLista() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<PagoResponseDTO> resultado = pagoService.listarPagos();

        assertEquals(1, resultado.size());
        verify(pagoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarPago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoResponseDTO resultado = pagoService.buscarPorId(1L);

        assertEquals(12000.0, resultado.getMonto());
        verify(pagoRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.buscarPorId(99L));
    }

    @Test
    void guardarPago_deberiaGuardarPago() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.guardarPago(request);

        assertEquals(1L, resultado.getId());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void actualizarPago_cuandoExiste_deberiaActualizarPago() {
        PagoRequestDTO actualizado = PagoRequestDTO.builder().agendaId(2L).clienteId(3L).monto(15000.0)
                .fechaPago(request.getFechaPago()).estadoPago("PAGADO").metodoPago("TARJETA").build();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoResponseDTO resultado = pagoService.actualizarPago(1L, actualizado);

        assertEquals(15000.0, resultado.getMonto());
        verify(pagoRepository).save(pago);
    }

    @Test
    void eliminarPago_cuandoExiste_deberiaEliminarPago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        pagoService.eliminarPago(1L);

        verify(pagoRepository).delete(pago);
    }
}
