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
import com.ajitech.teloconvierto.assemblers.ConversionModeloAssemblers;
import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.servicio.ConversionServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/conversiones")
public class ConversionControllerV2 {
    @Autowired
    private ConversionServicio conversionServicio;

    @Autowired
    private ConversionModeloAssemblers conversionAssembler;

    // Aquí puedes definir los endpoints para manejar Conversions, por ejemplo:
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    
    public ResponseEntity<CollectionModel<EntityModel<Conversion>>> listar() {
        // Obtiene todos los Conversions del servicio
        List<EntityModel<Conversion>> conversiones = conversionServicio.listar().stream()
                .map(conversionAssembler::toModel)
                .collect(Collectors.toList());
        if(conversiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                conversiones,
                linkTo(methodOn(ConversionControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Conversion>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un Conversion por su ID del servicio
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversionAssembler.toModel(conversion));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Conversion>> crearConversion(@RequestBody Conversion Conversion) {
        // Crea un nuevo Conversion
        Conversion nuevoConversion = conversionServicio.guardar(Conversion);
        return ResponseEntity
                .created(linkTo((methodOn(ConversionControllerV2.class).obtenerPorId(nuevoConversion.getId()))).toUri())
                .body(conversionAssembler.toModel(nuevoConversion));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Conversion>> actualizarConversion(@PathVariable Integer id,@Valid @RequestBody Conversion conversion) {
        // Actualiza un Conversion existente
        Conversion conversionExistente = conversionServicio.actualizarTodo(id, conversion);
        if (conversionExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(conversionAssembler.toModel(conversionExistente));
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    // Este método permite actualizar parcialmente un Conversion
    public ResponseEntity<EntityModel<Conversion>> patchConversion(@PathVariable Integer id, @Valid @RequestBody Conversion Conversion) {
        // Actualiza parcialmente un Conversion existente
        Conversion conversionExistente = conversionServicio.patchConversion(id, Conversion);
        if (conversionExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Solo actualiza los campos no nulos
        return ResponseEntity.ok(conversionAssembler.toModel(conversionExistente));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarConversion(@PathVariable Integer id) {
        // Elimina un Conversion por su ID
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        conversionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
