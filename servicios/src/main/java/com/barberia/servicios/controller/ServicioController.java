package com.barberia.servicios.controller;

import com.barberia.servicios.model.Servicio;
import com.barberia.servicios.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public List<Servicio> listarServicios() {
        return servicioService.listarServicios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> buscarPorId(@PathVariable Long id) {
        return servicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponible/{disponible}")
    public List<Servicio> buscarPorDisponible(@PathVariable Boolean disponible) {
        return servicioService.buscarPorDisponible(disponible);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Servicio> buscarPorNombre(@PathVariable String nombre) {
        return servicioService.buscarPorNombre(nombre);
    }

    @PostMapping
    public Servicio guardarServicio(@Valid @RequestBody Servicio servicio) {
        return servicioService.guardarServicio(servicio);
    }

    @PutMapping("/{id}")
    public Servicio modificarServicio(
            @PathVariable Long id,
            @Valid @RequestBody Servicio servicio
    ) {
        return servicioService.modificarServicio(id, servicio);
    }

    @DeleteMapping("/{id}")
    public String eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return "Servicio eliminado correctamente";
    }
}
