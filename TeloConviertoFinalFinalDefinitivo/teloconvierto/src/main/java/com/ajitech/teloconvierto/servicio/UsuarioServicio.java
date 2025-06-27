package com.ajitech.teloconvierto.servicio;

import com.ajitech.teloconvierto.modelo.Conversion;
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

    //@Autowired
    //private ConversionServicio conversionServicio; ESTA INYECCION DE LA INSTACIA PROVOCA ERROR PORQUE EN ConversionServicio TAMBIEN EXISTE UNA INSTANCIA DE UsuarioServicio Y ESO GENERA UN BUCLE

    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }
    
    public Usuario actualizarTodo(Integer id, Usuario usuario) { // actualizar todo
        Usuario existingUsuario = usuarioRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuario.getCorreoElectronico() != null) existingUsuario.setCorreoElectronico(usuario.getCorreoElectronico()); 
        if (usuario.getNombreUsuario() != null) existingUsuario.setNombreUsuario(usuario.getNombreUsuario());
        if (usuario.getClave() != null) existingUsuario.setClave(usuario.getClave());
        return usuarioRepositorio.save(existingUsuario);
    }

    public void eliminar(Integer id) {
        // Primero, verificar si el usuario existe
        Usuario usuario = usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // --- CAMBIO CLAVE AQUÍ ---
        // Obtener todas las conversiones asociadas a este usuario
        List<Conversion> conversiones = conversionRepositorio.findByUsuario(usuario);
        // Eliminar todas esas conversiones directamente a través del repositorio
        // Esto es más eficiente y rompe la dependencia circular.
        if (!conversiones.isEmpty()) {
            conversionRepositorio.deleteAll(conversiones);
        }
        // Finalmente, eliminar el usuario
        usuarioRepositorio.delete(usuario);
    }

    // METODO QUE GENERA EL ERROR SE ACTUALIZO EL ELIMINAR POR EL DE ARRIBA
    //public void eliminar(Integer id) {
        //Primero, verificar si el usuario existe
    //    Usuario usuario = usuarioRepositorio.findById(id)
    //        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    //    List<Conversion> conversiones = conversionRepositorio.findByUsuario(usuario);
    //    for(Conversion conversion : conversiones){
    //        conversionServicio.eliminar(conversion.getId());
    //    }
    //    usuarioRepositorio.delete(usuario);
   // }



    
    public Usuario patchUsuario(Integer id, Usuario usuario) {
        Usuario existingUsuario = obtenerPorId(id);
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
            return guardar(existingUsuario);
        }
        return null;
    }

    // los 2 metodos del Query y 1 personalizado
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepositorio.buscarPorNombre(nombre);
    }

    public List<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepositorio.buscarExactoPorCorreo(correo);
    }

    public List<Usuario> findByCorreoElectronicoAndNombreUsuario(String nombre, String correo) {
        return usuarioRepositorio.findByCorreoElectronicoAndNombreUsuario(nombre, correo);
    }
}