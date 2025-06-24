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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.assemblers.ArchivoModeloAssemblers;
import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.servicio.ArchivoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/archivos")
public class ArchivoControllerV2 {
    @Autowired
    private ArchivoServicio archivoServicio;

    @Autowired
    private ArchivoModeloAssemblers archivoAssembler;

    // Aquí puedes definir los endpoints para manejar Archivos, por ejemplo:
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    
    public ResponseEntity<CollectionModel<EntityModel<Archivo>>> listar() {
        // Obtiene todos los Archivos del servicio
        List<EntityModel<Archivo>> Archivos = archivoServicio.listar().stream()
                .map(archivoAssembler::toModel)
                .collect(Collectors.toList());
        if(Archivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                Archivos,
                linkTo(methodOn(ArchivoControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Archivo>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un Archivo por su ID del servicio
        Archivo Archivo = archivoServicio.obtenerPorId(id);
        if (Archivo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(archivoAssembler.toModel(Archivo));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Archivo>> crearArchivo(@RequestBody Archivo Archivo) {
        // Crea un nuevo Archivo
        Archivo nuevoArchivo = archivoServicio.guardar(Archivo);
        return ResponseEntity
                .created(linkTo((methodOn(ArchivoControllerV2.class).obtenerPorId(nuevoArchivo.getId()))).toUri())
                .body(archivoAssembler.toModel(nuevoArchivo));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Archivo>> actualizarArchivo(@PathVariable Integer id,@Valid @RequestBody Archivo archivo) {
        // Actualiza un archivo existente
        Archivo archivoExistente = archivoServicio.actualizarTodo(id, archivo);
        if (archivoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(archivoAssembler.toModel(archivoExistente));
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    // Este método permite actualizar parcialmente un Archivo
    public ResponseEntity<EntityModel<Archivo>> patchArchivo(@PathVariable Integer id, @Valid @RequestBody Archivo Archivo) {
        // Actualiza parcialmente un Archivo existente
        Archivo ArchivoExistente = archivoServicio.patchArchivo(id, Archivo);
        if (ArchivoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(archivoAssembler.toModel(ArchivoExistente));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarArchivo(@PathVariable Integer id) {
        // Elimina un Archivo por su ID
        Archivo Archivo = archivoServicio.obtenerPorId(id);
        if (Archivo == null) {
            return ResponseEntity.notFound().build();
        }
        archivoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }



}
