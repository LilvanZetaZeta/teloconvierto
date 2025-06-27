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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/conversiones")
@Tag(name="Conversiones", description ="Controlador de las conversiones")
public class ConversionControllerV2 {
    @Autowired
    private ConversionServicio conversionServicio;

    @Autowired
    private ConversionModeloAssemblers conversionAssembler;

    // Aquí puedes definir los endpoints para manejar Conversions, por ejemplo:
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api encuentra todas las conversiones", description = "Esta api encuentra todas las conversiones")
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
    @Operation(summary = "Esta api encuentra conversiones por id", description = "Esta api encuentra conversiones por id")
    public ResponseEntity<EntityModel<Conversion>> obtenerPorId(@RequestParam Integer id) {
        // Obtiene un Conversion por su ID del servicio
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversionAssembler.toModel(conversion));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api crea conversiones", description = "Esta api crea conversiones")
    public ResponseEntity<EntityModel<Conversion>> crearConversion(@RequestBody Conversion Conversion) {
        // Crea un nuevo Conversion
        Conversion nuevoConversion = conversionServicio.guardar(Conversion);
        return ResponseEntity
                .created(linkTo((methodOn(ConversionControllerV2.class).obtenerPorId(nuevoConversion.getId()))).toUri())
                .body(conversionAssembler.toModel(nuevoConversion));    
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Esta api actualiza las conversiones por el id", description = "Esta api actualiza las conversiones por id")
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
    @Operation(summary = "Esta api edita las conversiones por el id", description = "Esta api edita las conversiones por id")
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
    @Operation(summary = "Esta api elimina las conversiones por el id", description = "Esta api elimina las conversiones por id")
    public ResponseEntity<Void> eliminarConversion(@PathVariable Integer id) {
        // Elimina un Conversion por su ID
        Conversion conversion = conversionServicio.obtenerPorId(id);
        if (conversion == null) {
            return ResponseEntity.notFound().build();
        }
        conversionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //Metodos query y personalizados
    @GetMapping(value = "/buscar/usuario-extension", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar conversiones por usuario y extensión", description = "Busca conversiones por correo del usuario y extensión del formato convertido.")
    public ResponseEntity<CollectionModel<EntityModel<Conversion>>> buscarPorUsuarioYExtensionFormato(@RequestParam String correo, String extensionFormato) {
        // Busca usuarios por nombre
        List<EntityModel<Conversion>> usuarios = conversionServicio.buscarPorUsuarioYExtensionFormato(correo,extensionFormato).stream()
                .map(conversionAssembler::toModel)
                .collect(Collectors.toList());
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(ConversionControllerV2.class).buscarPorUsuarioYExtensionFormato(correo,extensionFormato)).withSelfRel()
        ));
    }
    
    @GetMapping(value = "/buscar/formato-convertido", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar conversiones por formato convertido", description = "Busca conversiones por el ID del formato convertido.")
    public ResponseEntity<CollectionModel<EntityModel<Conversion>>> buscarPorFormatoConvertido(@RequestParam Integer formatoId) {
        // Busca usuarios por nombre
        List<EntityModel<Conversion>> conversiones = conversionServicio.buscarPorFormatoConvertido(formatoId).stream()
                .map(conversionAssembler::toModel)
                .collect(Collectors.toList());
        if(conversiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                conversiones,
                linkTo(methodOn(ConversionControllerV2.class).buscarPorFormatoConvertido(formatoId)).withSelfRel()
        ));
    }

    @GetMapping(value = "/buscar/usuario-archivo", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar conversiones por usuario y archivo", description = "Busca conversiones por ID de usuario y ID de archivo original.")
    public ResponseEntity<CollectionModel<EntityModel<Conversion>>> buscarPorUsuarioIdAndArchivoOrigenId(@RequestParam Integer usuarioId, Integer archivoId) {
        // Busca usuarios por nombre
        List<EntityModel<Conversion>> conversiones = conversionServicio.findByUsuarioIdAndArchivoOrigenId(usuarioId, archivoId).stream()
                .map(conversionAssembler::toModel)
                .collect(Collectors.toList());
        if(conversiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                conversiones,
                linkTo(methodOn(ConversionControllerV2.class).buscarPorUsuarioIdAndArchivoOrigenId(usuarioId, archivoId)).withSelfRel()
        ));
    }
}
