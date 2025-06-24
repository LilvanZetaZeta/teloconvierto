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

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.servicio.ArchivoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/archivos")


public class ArchivoController {

    @Autowired
    private ArchivoServicio archivoServicio;

    @GetMapping
    public ResponseEntity<List<Archivo>> getAllArchivos() {
        List<Archivo> archivos = archivoServicio.listar();
        if (archivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Archivo> getArchivoById(@PathVariable Integer id) {
        Archivo archivo = archivoServicio.obtenerPorId(id);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(archivo);
    }

    @PostMapping
    public ResponseEntity<Archivo> createArchivo(@Valid @RequestBody Archivo archivo) {
        Archivo creado = archivoServicio.guardar(archivo);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Archivo> updateArchivo(@PathVariable Integer id,@Valid @RequestBody Archivo archivo) {
        Archivo actualizado = archivoServicio.actualizarTodo(id,archivo); 
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Archivo> patchArchivo(@PathVariable Integer id, @RequestBody Archivo archivo) {
        Archivo actualizado = archivoServicio.patchArchivo(id, archivo);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchivo(@PathVariable Integer id) {
        Archivo archivo = archivoServicio.obtenerPorId(id);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }
        archivoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //Query
    // Buscar archivos por nombre (LIKE) y extensión exacta
    @GetMapping("/buscar/nombre-extension")
    public ResponseEntity<List<Archivo>> buscarPorNombreYExtension(
            @RequestParam String nombre,
            @RequestParam String extension) {
        List<Archivo> archivos = archivoServicio.buscarPorNombreYExtension(nombre, extension);
        return ResponseEntity.ok(archivos);
    }

    // Buscar archivos por ID del formato
    @GetMapping("/buscar/formato")
    public ResponseEntity<List<Archivo>> buscarPorFormato(@RequestParam Integer formatoId) {
        List<Archivo> archivos = archivoServicio.buscarPorFormato(formatoId);
        return ResponseEntity.ok(archivos);
    }

    // Buscar archivos por ID de formato y nombre exacto
    @GetMapping("/buscar/formato-nombre")
    public ResponseEntity<List<Archivo>> buscarPorFormatoYNombre(
            @RequestParam Integer formatoId,
            @RequestParam String nombreArchivo) {
        List<Archivo> archivos = archivoServicio.findByFormatoIdAndNombreArchivo(formatoId, nombreArchivo);
        return ResponseEntity.ok(archivos);
    }
}
