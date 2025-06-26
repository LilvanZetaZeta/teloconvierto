package com.ajitech.teloconvierto.controlador.V2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.assemblers.UsuarioModeloAssemblers;
import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.servicio.UsuarioServicio;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioModeloAssemblers usuarioAssembler;

    // Aquí puedes definir los endpoints para manejar usuarios, por ejemplo:
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api encuentra todos los usuarios", description = "Esta api encuentra todos los usuarios")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listar() {
        // Obtiene todos los usuarios del servicio
        List<EntityModel<Usuario>> usuarios = usuarioServicio.listar().stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api encuentra usuarios por id", description = "Esta api encuentra usuarios por id")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un usuario por su ID del servicio
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api crea usuarios", description = "Esta api crea usuarios")
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario) {
        // Crea un nuevo usuario
        Usuario nuevoUsuario = usuarioServicio.guardar(usuario);
        return ResponseEntity
                .created(linkTo((methodOn(UsuarioControllerV2.class).obtenerPorId(nuevoUsuario.getId()))).toUri())
                .body(usuarioAssembler.toModel(nuevoUsuario));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api actualiza las conversiones por el id", description = "Esta api actualiza las conversiones por id")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Integer id,@Valid @RequestBody Usuario usuario) {
        // Actualiza un usuario existente
        Usuario usuarioExistente = usuarioServicio.actualizarTodo(id, usuario);
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(usuarioAssembler.toModel(usuarioExistente));
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api edita los usuarios por el id", description = "Esta api edita los usuarios por id")
    // Este método permite actualizar parcialmente un usuario
    public ResponseEntity<EntityModel<Usuario>> patchUsuario(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        // Actualiza parcialmente un usuario existente
        Usuario usuarioExistente = usuarioServicio.patchUsuario(id, usuario);
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(usuarioAssembler.toModel(usuarioExistente));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api elimina los usuarios por el id", description = "Esta api elimina los usuarios por id")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        // Elimina un usuario por su ID
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // metodos query
    @GetMapping(value = "/buscar/nombre", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar usuarios por nombre", description = "Busca usuarios cuyo nombre contenga el valor dado.")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarPorNombre(@RequestParam String nombre) {
        // Busca usuarios por nombre
        List<EntityModel<Usuario>> usuarios = usuarioServicio.buscarPorNombre(nombre).stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).buscarPorNombre(nombre)).withSelfRel()
        ));
    }

    @GetMapping(value = "/buscar/correo", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar usuarios por correo", description = "Busca usuarios por correo exacto.")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarPorCorreo(@RequestParam String correo) {
        // Busca usuarios por correo
        List<EntityModel<Usuario>> usuarios = usuarioServicio.buscarPorCorreo(correo).stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).buscarPorCorreo(correo)).withSelfRel()
        ));    
    }

    @GetMapping(value = "/buscar/correo-nombre", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar usuarios por correo y nombre", description = "Busca usuarios por correo y nombre exactos.")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarPorCorreoYNombre(@RequestParam String nombre, @RequestParam String correo) {
        // Busca usuarios por correo y nombre
        List<EntityModel<Usuario>> usuarios = usuarioServicio.findByCorreoElectronicoAndNombreUsuario(nombre, correo).stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).buscarPorCorreoYNombre(nombre, correo)).withSelfRel()
        ));
    }
}
