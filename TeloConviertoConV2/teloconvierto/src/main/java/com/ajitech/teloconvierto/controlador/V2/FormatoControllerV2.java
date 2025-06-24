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
import com.ajitech.teloconvierto.assemblers.FormatoModeloAssemblers;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.servicio.FormatoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/formatos")
public class FormatoControllerV2 {

    @Autowired
    private FormatoServicio formatoServicio;

    @Autowired
    private FormatoModeloAssemblers formatoAssembler;

    // Aquí puedes definir los endpoints para manejar formatos, por ejemplo:
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    
    public ResponseEntity<CollectionModel<EntityModel<Formato>>> listar() {
        // Obtiene todos los formatos del servicio
        List<EntityModel<Formato>> formatos = formatoServicio.listar().stream()
                .map(formatoAssembler::toModel)
                .collect(Collectors.toList());
        if(formatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                formatos,
                linkTo(methodOn(FormatoControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Formato>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un formato por su ID del servicio
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formatoAssembler.toModel(formato));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Formato>> crearFormato(@RequestBody Formato formato) {
        // Crea un nuevo formato
        Formato nuevoFormato = formatoServicio.guardar(formato);
        return ResponseEntity
                .created(linkTo((methodOn(FormatoControllerV2.class).obtenerPorId(nuevoFormato.getId()))).toUri())
                .body(formatoAssembler.toModel(nuevoFormato));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Formato>> actualizarFormato(@PathVariable Integer id,@Valid @RequestBody Formato formato) {
        // Actualiza un formato existente
        Formato formatoExistente = formatoServicio.actualizarTodo(id, formato);
        if (formatoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(formatoAssembler.toModel(formatoExistente));
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    // Este método permite actualizar parcialmente un formato
    public ResponseEntity<EntityModel<Formato>> patchFormato(@PathVariable Integer id, @Valid @RequestBody Formato formato) {
        // Actualiza parcialmente un formato existente
        Formato formatoExistente = formatoServicio.patchFormato(id, formato);
        if (formatoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(formatoAssembler.toModel(formatoExistente));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarFormato(@PathVariable Integer id) {
        // Elimina un formato por su ID
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        formatoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
