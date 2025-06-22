package com.ajitech.teloconvierto.servicio;

import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;
import com.ajitech.teloconvierto.repositorio.UsuarioRepositorio;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ConversionRepositorio conversionRepositorio;

    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    public Usuario findById(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void deleteById(Integer id) {
        // Primero, verificar si el usuario existe
        Usuario usuario = usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Eliminar las conversiones asociadas al usuario
        conversionRepositorio.deleteByUsuario(usuario);

        usuarioRepositorio.delete(usuario);
    }

    public Usuario patchUsuario(Integer id, Usuario usuario) {
        Usuario existingUsuario = findById(id);
        if (existingUsuario != null) {
            if (usuario.getCorreoElectronico() != null) {
                existingUsuario.setCorreoElectronico(usuario.getCorreoElectronico());
            }
            if (usuario.getNombreUsuario() != null) {
                existingUsuario.setNombreUsuario(usuario.getNombreUsuario());
            }
            if (usuario.getClave() != null) {
                existingUsuario.setClave(usuario.getClave());
            }
            return save(existingUsuario);
        }
        return null;
    }

// los 2 metodos del Query

    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepositorio.buscarPorNombre(nombre);
    }

    public List<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepositorio.buscarExactoPorCorreo(correo);
    }
}