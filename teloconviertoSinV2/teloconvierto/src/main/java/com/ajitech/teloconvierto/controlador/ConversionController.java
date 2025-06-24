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

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/conversiones")

public class ConversionController {

    @Autowired
    private ConversionServicio conversionServicio;

    @GetMapping
    public ResponseEntity<List<Conversion>> getAllConversiones() {
        List<Conversion> conversiones = conversionServicio.listar();
        if (conversiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(conversiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversion> getConversionById(@PathVariable Integer id) {
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversion);
    }

    @PostMapping
    public ResponseEntity<Conversion> createConversion(@Valid @RequestBody Conversion conversion) {
        Conversion creada = conversionServicio.guardar(conversion);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conversion> updateConversion(@PathVariable Integer id, @Valid @RequestBody Conversion conversion) {
        Conversion actualizada = conversionServicio.actualizarTodo(id, conversion);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Conversion> patchConversion(@PathVariable Integer id, @RequestBody Conversion conversion) {
        Conversion actualizada = conversionServicio.patchConversion(id, conversion);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversion(@PathVariable Integer id) {
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        conversionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //Query
    // Buscar conversiones por correo del usuario y extensión del formato convertido (LIKE %extension%)
    @GetMapping("/buscar/usuario-extension")
    public ResponseEntity<List<Conversion>> buscarPorUsuarioYExtensionFormato(
            @RequestParam String correo,
            @RequestParam String extensionFormato) {
        List<Conversion> conversiones = conversionServicio.buscarPorUsuarioYExtensionFormato(correo, extensionFormato);
        return ResponseEntity.ok(conversiones);
    }

    // Buscar conversiones por ID de formato convertido
    @GetMapping("/buscar/formato-convertido")
    public ResponseEntity<List<Conversion>> buscarPorFormatoConvertido(@RequestParam Integer formatoId) {
        List<Conversion> conversiones = conversionServicio.buscarPorFormatoConvertido(formatoId);
        return ResponseEntity.ok(conversiones);
    }

    // Buscar conversiones por ID de usuario y ID de archivo original
    @GetMapping("/buscar/usuario-archivo")
    public ResponseEntity<List<Conversion>> buscarPorUsuarioYArchivo(
            @RequestParam Integer usuarioId,
            @RequestParam Integer archivoId) {
        List<Conversion> conversiones = conversionServicio.findByUsuarioIdAndArchivoOrigenId(usuarioId, archivoId);
        return ResponseEntity.ok(conversiones);
    }

}
