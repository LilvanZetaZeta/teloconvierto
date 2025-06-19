package com.ajitech.teloconvierto.controlador;



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
import org.springframework.web.bind.annotation.RequestParam;

import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.servicio.FormatoServicio;

@RestController
@RequestMapping("api/v1/formatos")

public class FormatoControlador {

    @Autowired
    private FormatoServicio formatoServicio;

    @PostMapping
    public ResponseEntity<Formato> crear(@RequestBody Formato formato) {
        return ResponseEntity.ok(formatoServicio.guardar(formato));
    }

    @GetMapping
    public ResponseEntity<?> buscarFormatos(@RequestParam(required = false) String nombre, @RequestParam(required = false) String extension) {
        if (nombre != null) {
            return ResponseEntity.ok(formatoServicio.buscarPorNombre(nombre));
        } else if (extension != null) {
            return ResponseEntity.ok(formatoServicio.buscarPorExtension(extension));
        } else {
            return ResponseEntity.ok(formatoServicio.listar());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Formato> obtener(@PathVariable Integer id) {
        return formatoServicio.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formato> actualizar(@PathVariable Integer id, @RequestBody Formato formato) {
        formato.setId(id);
        return ResponseEntity.ok(formatoServicio.actualizar(formato));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Formato> modificar(@PathVariable Integer id, @RequestBody Formato formato) {
        formato.setId(id);
        return ResponseEntity.ok(formatoServicio.actualizar(formato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        formatoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

