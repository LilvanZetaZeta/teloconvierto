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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/formatos")

public class FormatoController {

    @Autowired
    private FormatoServicio formatoServicio;

    @GetMapping
    @Operation(summary = "Esta api encuentra todos los formatos", description = "Esta api encuentra todos los formatos")
    public ResponseEntity<List<Formato>> getAllFormatos() {
        List<Formato> formatos = formatoServicio.listar();
        if (formatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(formatos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Esta api encuentra formatos por id", description = "Esta api encuentra formatos por id")
    public ResponseEntity<Formato> getFormatoById(@PathVariable Integer id) {
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formato);
    }

    @PostMapping
    @Operation(summary = "Esta api crea formatos", description = "Esta api crea formatos")
    public ResponseEntity<Formato> createFormato(@Valid @RequestBody Formato formato) {
        Formato creado = formatoServicio.guardar(formato);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Esta api actualiza las conversiones por el id", description = "Esta api actualiza las conversiones por id")
    public ResponseEntity<Formato> updateFormato(@PathVariable Integer id,@Valid @RequestBody Formato formato) {
        Formato actualizado = formatoServicio.actualizarTodo(id,formato);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Esta api edita los formatos por el id", description = "Esta api edita los formatos por id")
    public ResponseEntity<Formato> patchFormato(@PathVariable Integer id, @RequestBody Formato formato) {
        Formato actualizado = formatoServicio.patchFormato(id, formato);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Esta api elimina los formatos por el id", description = "Esta api elimina los formatos por id")
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
    @Operation(summary = "Buscar formatos por nombre", description = "Busca formatos cuyo nombre contenga el valor dado.")
    public ResponseEntity<List<Formato>> buscarPorNombre(@RequestParam String nombre) {
        List<Formato> formatos = formatoServicio.buscarPorNombre(nombre);
        return ResponseEntity.ok(formatos);
    }

    // Buscar formatos por extensión exacta
    @GetMapping("/buscar/extension")
    @Operation(summary = "Buscar formatos por extensión", description = "Busca formatos por extensión exacta.")
    public ResponseEntity<List<Formato>> buscarPorExtension(@RequestParam String extension) {
        List<Formato> formatos = formatoServicio.buscarPorExtension(extension);
        return ResponseEntity.ok(formatos);
    }

    // Buscar formatos por nombre y extensión exactos
    @GetMapping("/buscar/nombre-extension")
    @Operation(summary = "Buscar formatos por nombre y extensión", description = "Busca formatos por nombre y extensión exactos.")
    public ResponseEntity<List<Formato>> buscarPorNombreYExtension(
            @RequestParam String nombreFormato,
            @RequestParam String extensionFormato) {
        List<Formato> formatos = formatoServicio.findByNombreFormatoAndExtensionFormato(nombreFormato, extensionFormato);
        return ResponseEntity.ok(formatos);
    }
}