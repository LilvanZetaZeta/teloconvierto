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


import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.servicio.UsuarioServicio;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioServicio.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario creado = usuarioServicio.guardar(usuario);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id,@Valid @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioServicio.actualizarTodo(id, usuario);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> patchUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioServicio.patchUsuario(id, usuario);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // metodos query
    // Buscar usuarios por nombre (LIKE %nombre%)
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam String nombre) {
        List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuarios por correo exacto
    @GetMapping("/buscar/correo")
    public ResponseEntity<List<Usuario>> buscarPorCorreo(@RequestParam String correo) {
        List<Usuario> usuarios = usuarioServicio.buscarPorCorreo(correo);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuarios por correo Y nombre exactos
    @GetMapping("/buscar/correo-nombre")
    public ResponseEntity<List<Usuario>> buscarPorCorreoYNombre(
            @RequestParam String correo,
            @RequestParam String nombreUsuario) {
        List<Usuario> usuarios = usuarioServicio.findByCorreoElectronicoAndNombreUsuario(correo, nombreUsuario);
        return ResponseEntity.ok(usuarios);
    }
}