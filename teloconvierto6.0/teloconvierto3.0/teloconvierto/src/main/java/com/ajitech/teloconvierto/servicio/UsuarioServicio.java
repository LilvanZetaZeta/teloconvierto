package com.ajitech.teloconvierto.servicio;

import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.repositorio.UsuarioRepositorio;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepositorio.findById(id);
    }
   
    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

     public List<Usuario> buscarPorNombre(String nombre) {
    return usuarioRepositorio.buscarPorNombre(nombre);
    }

    public List<Usuario> buscarPorCorreoExacto(String correo) {
    return usuarioRepositorio.buscarExactoPorCorreo(correo);
    }

}