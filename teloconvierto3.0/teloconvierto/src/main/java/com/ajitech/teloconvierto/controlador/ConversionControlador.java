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
import org.springframework.web.bind.annotation.RequestParam;

import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.servicio.ConversionServicio;

@RestController
@RequestMapping("api/v1/conversiones")

public class ConversionControlador {

    @Autowired
    private ConversionServicio conversionServicio;

    @PostMapping
    public ResponseEntity<Conversion> crear(@RequestBody Conversion conversion) {
        return ResponseEntity.ok(conversionServicio.guardar(conversion));
    }

    @GetMapping
    public ResponseEntity<?> buscarConversiones(@RequestParam(required = false) Integer formatoOrigenId,
                                            @RequestParam(required = false) Integer formatoConvertidoId) {
    if (formatoOrigenId != null) {
        return ResponseEntity.ok(conversionServicio.buscarPorFormatoOrigen(formatoOrigenId));
    } else if (formatoConvertidoId != null) {
        return ResponseEntity.ok(conversionServicio.buscarPorFormatoConvertido(formatoConvertidoId));
    } else {
        return ResponseEntity.ok(conversionServicio.listar());
    }
    }
    
    

    @GetMapping("/{id}")
    public ResponseEntity<Conversion> obtener(@PathVariable Integer id) {
        return conversionServicio.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Conversion>> porUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(conversionServicio.obtenerPorUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conversion> actualizar(@PathVariable Integer id, @RequestBody Conversion conversion) {
        conversion.setId(id);
        return ResponseEntity.ok(conversionServicio.actualizar(conversion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Conversion> modificar(@PathVariable Integer id, @RequestBody Conversion conversion) {
        conversion.setId(id);
        return ResponseEntity.ok(conversionServicio.actualizar(conversion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        conversionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
