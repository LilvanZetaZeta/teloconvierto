package com.ajitech.teloconvierto.controlador;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.servicio.FormatoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/formatos")

public class FormatoController {

    @Autowired
    private FormatoServicio formatoServicio;

    @GetMapping
    public ResponseEntity<List<Formato>> getAllFormatos() {
        List<Formato> formatos = formatoServicio.listar();
        if (formatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(formatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formato> getFormatoById(@PathVariable Integer id) {
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formato);
    }

    @PostMapping
    public ResponseEntity<Formato> createFormato(@Valid @RequestBody Formato formato) {
        Formato creado = formatoServicio.guardar(formato);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formato> updateFormato(@PathVariable Integer id,@Valid @RequestBody Formato formato) {
        Formato actualizado = formatoServicio.actualizarTodo(id,formato);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Formato> patchFormato(@PathVariable Integer id, @RequestBody Formato formato) {
        Formato actualizado = formatoServicio.patchFormato(id, formato);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormato(@PathVariable Integer id) {
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        formatoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // metodos query y personalizados
    // Buscar formatos por nombre (LIKE %nombre%)
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Formato>> buscarPorNombre(@RequestParam String nombre) {
        List<Formato> formatos = formatoServicio.buscarPorNombre(nombre);
        return ResponseEntity.ok(formatos);
    }

    // Buscar formatos por extensión exacta
    @GetMapping("/buscar/extension")
    public ResponseEntity<List<Formato>> buscarPorExtension(@RequestParam String extension) {
        List<Formato> formatos = formatoServicio.buscarPorExtension(extension);
        return ResponseEntity.ok(formatos);
    }

    // Buscar formatos por nombre y extensión exactos
    @GetMapping("/buscar/nombre-extension")
    public ResponseEntity<List<Formato>> buscarPorNombreYExtension(
            @RequestParam String nombreFormato,
            @RequestParam String extensionFormato) {
        List<Formato> formatos = formatoServicio.findByNombreFormatoAndExtensionFormato(nombreFormato, extensionFormato);
        return ResponseEntity.ok(formatos);
    }
}