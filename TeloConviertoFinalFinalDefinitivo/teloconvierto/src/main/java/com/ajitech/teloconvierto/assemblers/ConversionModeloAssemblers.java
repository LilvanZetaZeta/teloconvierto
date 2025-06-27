package com.ajitech.teloconvierto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.controlador.V2.ConversionControllerV2;
import com.ajitech.teloconvierto.modelo.Conversion;


@Component
public class ConversionModeloAssemblers implements RepresentationModelAssembler<Conversion, EntityModel<Conversion>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel<Conversion> toModel(Conversion conversion) {
        return EntityModel.of(conversion,
            linkTo(methodOn(ConversionControllerV2.class).obtenerPorId(conversion.getId())).withSelfRel(),
            linkTo(methodOn(ConversionControllerV2.class).listar()).withRel("listar todos los Conversions"),
            linkTo(methodOn(ConversionControllerV2.class).actualizarConversion(conversion.getId(), conversion)).withRel("actualizar Conversion"),
            linkTo(methodOn(ConversionControllerV2.class).eliminarConversion(conversion.getId())).withRel("eliminar Conversion"),
            linkTo(methodOn(ConversionControllerV2.class).patchConversion(conversion.getId(), conversion)).withRel("actualizacion parcial del Conversion"),
            // metodos query y personalizado
            linkTo(methodOn(ConversionControllerV2.class).buscarPorUsuarioYExtensionFormato(conversion.getUsuario().getCorreoElectronico(), conversion.getFormatoConvertido().getExtensionFormato())).withRel("buscar por nombre de usuario y extension formato"),
            linkTo(methodOn(ConversionControllerV2.class).buscarPorFormatoConvertido(conversion.getFormatoConvertido().getId())).withRel("buscar por formato convertido de Conversion"),
            linkTo(methodOn(ConversionControllerV2.class).buscarPorUsuarioIdAndArchivoOrigenId(conversion.getUsuario().getId(), conversion.getArchivoOrigen().getId())).withRel("buscar por usuario y archivo origen")
        );
    }
}
