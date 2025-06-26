package com.ajitech.teloconvierto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.controlador.V2.FormatoControllerV2;



@Component
public class FormatoModeloAssemblers implements RepresentationModelAssembler<Formato, EntityModel<Formato>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Formato> toModel(Formato formato) {
        return EntityModel.of(formato,
            linkTo(methodOn(FormatoControllerV2.class).obtenerPorId(formato.getId())).withSelfRel(),
            linkTo(methodOn(FormatoControllerV2.class).listar()).withRel("listar todos los Formatos"),
            linkTo(methodOn(FormatoControllerV2.class).actualizarFormato(formato.getId(), formato)).withRel("actualizar Formato"),
            linkTo(methodOn(FormatoControllerV2.class).eliminarFormato(formato.getId())).withRel("eliminar Formato"),
            linkTo(methodOn(FormatoControllerV2.class).patchFormato(formato.getId(), formato)).withRel("actualizacion parcial del Formato"),
            // metodos query y personalizado
            linkTo(methodOn(FormatoControllerV2.class).buscarPorNombre(formato.getNombreFormato())).withRel("buscar por nombre de Formato"),
            linkTo(methodOn(FormatoControllerV2.class).buscarPorExtension(formato.getExtensionFormato())).withRel("buscar por extension de formato"),
            linkTo(methodOn(FormatoControllerV2.class).buscarPorNombreYExtension(formato.getNombreFormato(),formato.getExtensionFormato())).withRel("buscar por nombre y extension de Formato")
        );
    }
}
