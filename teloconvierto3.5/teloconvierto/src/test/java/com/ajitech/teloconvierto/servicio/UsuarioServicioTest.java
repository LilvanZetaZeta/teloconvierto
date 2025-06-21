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

    private Usuario crearusuario(){
        return new Usuario(1, "pepito@gmail.com", "elpepe", "123");
    }

    @Test
    public void testListar() {
        when(usuarioRepositorio.findAll()).thenReturn(List.of(crearusuario()));
        List<Usuario> usuarios = usuarioServicio.findAll();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }
    
    @Test
    public void testListarPorId() {
        Usuario usuario = crearusuario();
        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(usuario));
        Usuario foundusuario = usuarioServicio.findById(1); // revisar
        assertNotNull(foundusuario);
        assertEquals("elpepe", foundusuario.getNombreUsuario());
    }

    @Test 
    public void testGuardar() {
        Usuario usuario = crearusuario();
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);
        Usuario savedusuario = usuarioServicio.save(usuario);
        assertNotNull(savedusuario);
        assertEquals("elpepe", savedusuario.getNombreUsuario());
    }

    @Test
    public void testPatcActualizar() {
        Usuario existing = crearusuario();  // nombre: "elpepe"
        Usuario patchData = new Usuario();
        patchData.setNombreUsuario("pedro");  // nuevo nombre
        
        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(existing));
        when(usuarioRepositorio.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Usuario result = usuarioServicio.patchUsuario(1, patchData);
        
        assertNotNull(result);
        assertEquals("pedro", result.getNombreUsuario());
        
        verify(usuarioRepositorio, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer Id = 1;
        doNothing().when(usuarioRepositorio).deleteById(Id);;
        usuarioServicio.deleteById(Id);
        verify(usuarioRepositorio, times(1)).deleteById(Id);
    }

    @Test
    public void testBuscarPorNombre() {
        String nombre = "elpepe";
        List<Usuario> usuariosMock = List.of(crearusuario());
        when(usuarioRepositorio.buscarPorNombre(nombre)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("elpepe", usuarios.get(0).getNombreUsuario());
    }

    @Test
    public void testBuscarPorCorreo() {
        String correo = "pepito@gmail.com";
        List<Usuario> usuariosMock = List.of(crearusuario());
        when(usuarioRepositorio.buscarExactoPorCorreo(correo)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorCorreo(correo);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("pepito@gmail.com", usuarios.get(0).getCorreoElectronico());
    }

    // QUERY TESTS

}
