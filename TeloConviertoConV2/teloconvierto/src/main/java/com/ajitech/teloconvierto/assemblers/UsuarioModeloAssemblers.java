package com.ajitech.teloconvierto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.ajitech.teloconvierto.controlador.V2.UsuarioControllerV2;
import com.ajitech.teloconvierto.modelo.Usuario;

@Component
public class UsuarioModeloAssemblers implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    //@SuppressWarnings("null")
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).listar()).withRel("listar todos los usuarios"),
            linkTo(methodOn(UsuarioControllerV2.class).actualizarUsuario(usuario.getId(), usuario)).withRel("actualizar usuario"),
            linkTo(methodOn(UsuarioControllerV2.class).eliminarUsuario(usuario.getId())).withRel("eliminar usuario"),
            linkTo(methodOn(UsuarioControllerV2.class).patchUsuario(usuario.getId(), usuario)).withRel("actualizacion parcial del usuario"),
            // metodos query y personalizado
            linkTo(methodOn(UsuarioControllerV2.class).buscarPorNombre(usuario.getNombreUsuario())).withRel("buscar por nombre de usuario"),
            linkTo(methodOn(UsuarioControllerV2.class).buscarPorCorreo(usuario.getCorreoElectronico())).withRel("buscar por correo electronico"),
            linkTo(methodOn(UsuarioControllerV2.class).buscarPorCorreoYNombre(usuario.getCorreoElectronico(), usuario.getNombreUsuario())).withRel("buscar por correo y nombre de usuario")

        );

    }
}
