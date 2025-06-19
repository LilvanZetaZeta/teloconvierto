package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.repositorio.UsuarioRepositorio;
import java.util.List;

@SpringBootTest
public class UsuarioServicioTest {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @MockBean
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    public void testListar() {
        when(usuarioRepositorio.findAll()).thenReturn(List.of(new Usuario(1, "pepito@gmail.com", "elpepe", "123")));
        List<Usuario> usuarios = usuarioServicio.listar();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }
    
    @Test
    public void testListarPorId() {
        Usuario usuario = new Usuario(1, "pepe123@gmail.com", "pepepro", "111");
        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(usuario));
        Usuario foundusuario = usuarioServicio.obtenerPorId(1).orElse(null); // revisar
        assertNotNull(foundusuario);
        assertEquals("Ingeniería", foundusuario.getNombreUsuario());
    }

    @Test 
    public void testGuardar() {
        Usuario usuario = new Usuario(1, "pepe123@gmail.com", "pepepro", "111");
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);
        Usuario savedusuario = usuarioServicio.guardar(usuario);
        assertNotNull(savedusuario);
        assertEquals("pepe", savedusuario.getNombreUsuario());
    }

    @Test
    public void testPatcActualizar() {
        Usuario existing = new Usuario(1, "pepe123@gmail.com", "pepepro", "111");
        Usuario patchData = new Usuario();
        patchData.setNombreUsuario("pedro");;

        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(existing));
        when(usuarioRepositorio.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioServicio.actualizar(patchData);

        assertNotNull(result);
        assertEquals("pablo", result.getNombreUsuario());
        verify(usuarioRepositorio, times(1)).save(existing);
    }

    @Test
    public void testEliminarPorId() {
        Integer Id = 1;
        doNothing().when(usuarioRepositorio).deleteById(Id);;
        usuarioServicio.eliminar(Id);
        verify(usuarioRepositorio, times(1)).deleteById(Id);
    }

    @Test
    public void testBuscarPorNombre() {
        String nombre = "elpepe";
        List<Usuario> usuariosMock = List.of(new Usuario(1, "pepito@gmail.com", "elpepe", "123"));
        when(usuarioRepositorio.buscarPorNombre(nombre)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("elpepe", usuarios.get(0).getNombreUsuario());
    }

    @Test
    public void testBuscarPorCorreo() {
        String correo = "pepito@gmail.com";
        List<Usuario> usuariosMock = List.of(new Usuario(1, "pepito@gmail.com", "elpepe", "123"));
        when(usuarioRepositorio.buscarExactoPorCorreo(correo)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorCorreoExacto(correo);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("pepito@gmail.com", usuarios.get(0).getCorreoElectronico());
    }
}
