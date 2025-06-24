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

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Esta api encuentra todos los formatos", description = "Esta api encuentra todos los formatos")
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
    @Operation(summary = "Esta api encuentra formatos por id", description = "Esta api encuentra formatos por id")
    public ResponseEntity<EntityModel<Formato>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un formato por su ID del servicio
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formatoAssembler.toModel(formato));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api crea formatos", description = "Esta api crea formatos")
    public ResponseEntity<EntityModel<Formato>> crearFormato(@RequestBody Formato formato) {
        // Crea un nuevo formato
        Formato nuevoFormato = formatoServicio.guardar(formato);
        return ResponseEntity
                .created(linkTo((methodOn(FormatoControllerV2.class).obtenerPorId(nuevoFormato.getId()))).toUri())
                .body(formatoAssembler.toModel(nuevoFormato));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api actualiza las conversiones por el id", description = "Esta api actualiza las conversiones por id")
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
    @Operation(summary = "Esta api edita los formatos por el id", description = "Esta api edita los formatos por id")
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
    @Operation(summary = "Esta api elimina los formatos por el id", description = "Esta api elimina los formatos por id")
    public ResponseEntity<Void> eliminarFormato(@PathVariable Integer id) {
        // Elimina un formato por su ID
        Formato formato = formatoServicio.obtenerPorId(id);
        if (formato == null) {
            return ResponseEntity.notFound().build();
        }
        formatoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // metodos query
    @GetMapping(value = "/buscar/nombre", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar formatos por nombre", description = "Busca formatos cuyo nombre contenga el valor dado.")
    public ResponseEntity<CollectionModel<EntityModel<Formato>>> buscarPorNombre(@RequestParam String nombre) {
        List<EntityModel<Formato>> formatos = formatoServicio.buscarPorNombre(nombre).stream()
                .map(formatoAssembler::toModel)
                .collect(Collectors.toList());
        if (formatos.isEmpty()) {
            return ResponseEntity.noContent().build();

        }
        return ResponseEntity.ok(CollectionModel.of(
                formatos,
                linkTo(methodOn(FormatoControllerV2.class).buscarPorNombre(nombre)).withSelfRel())
        );
    }

    @GetMapping(value = "/buscar/extension", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar formatos por extensión", description = "Busca formatos por extensión exacta.")
    public ResponseEntity<CollectionModel<EntityModel<Formato>>> buscarPorExtension(@RequestParam String extension) {
        List<EntityModel<Formato>> formatos = formatoServicio.buscarPorExtension(extension).stream()
                .map(formatoAssembler::toModel)
                .collect(Collectors.toList());
        if (formatos.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(CollectionModel.of(
                formatos,
                linkTo(methodOn(FormatoControllerV2.class).buscarPorExtension(extension)).withSelfRel())
        );
    }

    @GetMapping(value = "/buscar/nombre-extension", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar formatos por nombre y extensión", description = "Busca formatos por nombre y extensión exactos.")
    public ResponseEntity<CollectionModel<EntityModel<Formato>>> buscarPorNombreYExtension(@RequestParam String nombre, @RequestParam String extension) {
        List<EntityModel<Formato>> formatos = formatoServicio.findByNombreFormatoAndExtensionFormato(nombre, extension).stream()
                .map(formatoAssembler::toModel)
                .collect(Collectors.toList());
        if(formatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                formatos,
                linkTo(methodOn(FormatoControllerV2.class).buscarPorNombreYExtension(nombre, extension)).withSelfRel()
        ));
    }

}
