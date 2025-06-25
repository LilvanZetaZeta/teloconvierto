package com.ajitech.teloconvierto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.controlador.V2.ArchivoControllerV2;
import com.ajitech.teloconvierto.modelo.Archivo;

@Component
public class ArchivoModeloAssemblers implements RepresentationModelAssembler<Archivo, EntityModel<Archivo>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel<Archivo> toModel(Archivo archivo) {
        return EntityModel.of(archivo,
            linkTo(methodOn(ArchivoControllerV2.class).obtenerPorId(archivo.getId())).withSelfRel(),
            linkTo(methodOn(ArchivoControllerV2.class).listar()).withRel("listar todos los Archivos"),
            linkTo(methodOn(ArchivoControllerV2.class).actualizarArchivo(archivo.getId(), archivo)).withRel("actualizar Archivo"),
            linkTo(methodOn(ArchivoControllerV2.class).eliminarArchivo(archivo.getId())).withRel("eliminar Archivo"),
            linkTo(methodOn(ArchivoControllerV2.class).patchArchivo(archivo.getId(), archivo)).withRel("actualizacion parcial del Archivo"),
            // metodos query y personalizado
             // metodos query y personalizado
            linkTo(methodOn(ArchivoControllerV2.class).buscarPorNombreYExtension(archivo.getNombreArchivo(), archivo.getFormato().getExtensionFormato())).withRel("buscar por nombre de archivo y extension de formato"),
            linkTo(methodOn(ArchivoControllerV2.class).buscarPorFormato(archivo.getFormato().getId())).withRel("buscar por formato convertido de Archivo"),
            linkTo(methodOn(ArchivoControllerV2.class).buscarPorFormatoYNombre(archivo.getFormato().getId(), archivo.getNombreArchivo())).withRel("buscar por formato y nombre de Archivo")
           
        );
    }

}
