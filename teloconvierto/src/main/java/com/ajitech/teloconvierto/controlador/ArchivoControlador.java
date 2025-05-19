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
import org.springframework.web.bind.annotation.RestController;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.servicio.ArchivoServicio;

@RestController
@RequestMapping("api/v1/archivos")


public class ArchivoControlador {

    @Autowired
    private ArchivoServicio archivoServicio;

    @PostMapping
    public ResponseEntity<Archivo> crear(@RequestBody Archivo archivo) {
        return ResponseEntity.ok(archivoServicio.guardar(archivo));
    }

    

    @GetMapping
    public ResponseEntity<List<Archivo>> listar() {
        return ResponseEntity.ok(archivoServicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Archivo> obtener(@PathVariable Integer id) {
        return archivoServicio.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Archivo> actualizar(@PathVariable Integer id, @RequestBody Archivo archivo) {
        archivo.setId(id);
        return ResponseEntity.ok(archivoServicio.actualizar(archivo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Archivo> modificar(@PathVariable Integer id, @RequestBody Archivo archivo) {
        archivo.setId(id);
        return ResponseEntity.ok(archivoServicio.actualizar(archivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        archivoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}