package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.Constelacion;
import com.example.demo.repository.ConstelacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/constelaciones")
public class CatalogoController {
    private final ConstelacionRepository repositorio;

    public CatalogoController(ConstelacionRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Constelacion> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public Constelacion obtener(@PathVariable Long id) {
        return buscar(id);
    }

    @PostMapping
    public ResponseEntity<Constelacion> crear(@RequestBody DatosConstelacion datos) {
        validar(datos);
        Constelacion nueva = new Constelacion(datos.nombre().trim(), datos.hemisferio(), datos.estrellaPrincipal().trim(),
                textoOpcional(datos.descubiertoPor()));
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(nueva));
    }

    @PutMapping("/{id}")
    public Constelacion actualizar(@PathVariable Long id, @RequestBody DatosConstelacion datos) {
        Constelacion actual = buscar(id);
        validar(datos);
        actual.setNombre(datos.nombre().trim());
        actual.setHemisferio(datos.hemisferio());
        actual.setEstrellaPrincipal(datos.estrellaPrincipal().trim());
        actual.setDescubiertoPor(textoOpcional(datos.descubiertoPor()));
        return repositorio.save(actual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repositorio.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Constelacion buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Constelación no encontrada"));
    }

    private void validar(DatosConstelacion datos) {
        if (datos.nombre() == null || datos.nombre().isBlank() || datos.nombre().length() > 100
                || datos.estrellaPrincipal() == null || datos.estrellaPrincipal().isBlank()
                || datos.estrellaPrincipal().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre y estrella principal son obligatorios y admiten hasta 100 caracteres");
        }
        if (datos.hemisferio() == null || !List.of("Norte", "Sur", "Ambos").contains(datos.hemisferio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El hemisferio debe ser Norte, Sur o Ambos");
        }
        if (datos.descubiertoPor() != null && datos.descubiertoPor().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descubierto por admite hasta 100 caracteres");
        }
    }

    // Campo opcional: si viene vacío se guarda como null.
    private String textoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    public record DatosConstelacion(String nombre, String hemisferio, String estrellaPrincipal, String descubiertoPor) {}
}

