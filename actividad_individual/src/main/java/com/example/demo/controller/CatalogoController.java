//./mvnw spring-boot:run
package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/constelaciones")
public class CatalogoController {

    private final List<Constelacion> catalogo = new ArrayList<>();
    private long siguienteId = 1;

    public CatalogoController() {
        catalogo.add(new Constelacion(siguienteId++, "Orión", "Ori", "Ambos", "Rigel", "El cazador Orión"));
        catalogo.add(new Constelacion(siguienteId++, "Cruz del Sur", "Cru", "Sur", "Acrux", "La cruz del cielo austral"));
    }

    @GetMapping
    public List<Constelacion> listar() {
        return catalogo;
    }

    @PostMapping
    public ResponseEntity<Constelacion> registrar(@RequestBody NuevaConstelacion nueva) {
        if (nueva.nombre() == null || nueva.nombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        Constelacion constelacion = new Constelacion(
                siguienteId++, nueva.nombre(), nueva.abreviatura(), nueva.hemisferio(),
                nueva.estrellaPrincipal(), nueva.mitologia()
        );
        catalogo.add(constelacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(constelacion);
    }

    public record NuevaConstelacion(
            String nombre, String abreviatura, String hemisferio,
            String estrellaPrincipal, String mitologia
    ) {}

    public record Constelacion(
            long id, String nombre, String abreviatura, String hemisferio,
            String estrellaPrincipal, String mitologia
    ) {}
}
