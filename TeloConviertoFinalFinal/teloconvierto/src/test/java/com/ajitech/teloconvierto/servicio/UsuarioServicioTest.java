package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;
import com.ajitech.teloconvierto.repositorio.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UsuarioServicioTest {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @MockBean
    private UsuarioRepositorio usuarioRepositorio;

    @MockBean
    private ConversionRepositorio conversionRepositorio;

    private Usuario crearUsuario(){
        return new Usuario(1, "pepito@gmail.com", "elpepe", "123");
    }

    @Test
    public void testListar() {
        when(usuarioRepositorio.findAll()).thenReturn(List.of(crearUsuario()));
        List<Usuario> usuarios = usuarioServicio.listar();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }
    
    @Test
    public void testListarPorId() {
        Usuario usuario = crearUsuario();
        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(usuario));
        Usuario foundusuario = usuarioServicio.obtenerPorId(1); // revisar
        assertNotNull(foundusuario);
        assertEquals("elpepe", foundusuario.getNombreUsuario());
    }

    @Test 
    public void testGuardar() {
        Usuario usuario = crearUsuario();
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);
        Usuario savedusuario = usuarioServicio.guardar(usuario);
        assertNotNull(savedusuario);
        assertEquals("elpepe", savedusuario.getNombreUsuario());
        verify(usuarioRepositorio, times(1)).save(any(Usuario.class));
    }

    // testActualizarTodo
    @Test
    public void testActualizarTodo() {
        Usuario usuario = crearUsuario();  // nombre: "elpepe"
        Usuario nuevo = new Usuario();
        nuevo.setId(1);  // id existente
        nuevo.setCorreoElectronico("correo11@gmail.com");
        nuevo.setNombreUsuario("pepe");  // nuevo nombre
        nuevo.setClave("mmita1");
        
        Mockito.when(usuarioRepositorio.findById(1)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepositorio.save(Mockito.any(Usuario.class))).thenReturn(nuevo);
        
        Usuario actualizado = usuarioServicio.actualizarTodo(1, usuario);
        
        assertNotNull(actualizado);
        assertEquals(1, actualizado.getId());
        // Verifica que los campos se hayan actualizado correctamente
        assertEquals("correo11@gmail.com",actualizado.getCorreoElectronico());
        assertEquals("pepe", actualizado.getNombreUsuario());
        assertEquals("mmita1", actualizado.getClave());
        verify(usuarioRepositorio, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testPatchActualizar() {
        Usuario existingUsuario = crearUsuario();  // nombre: "elpepe"
        Usuario patchData = new Usuario();
        patchData.setNombreUsuario("pedro");  // nuevo nombre
        
        when(usuarioRepositorio.findById(1)).thenReturn(java.util.Optional.of(existingUsuario));
        when(usuarioRepositorio.save(any(Usuario.class))).thenReturn(patchData);
        
        Usuario actualizado = usuarioServicio.patchUsuario(1, patchData);
        assertNotNull(actualizado);
        assertEquals("pedro", actualizado.getNombreUsuario());
        
        verify(usuarioRepositorio, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer Id = 1;
        Usuario usuario = crearUsuario();
        usuario.setId(Id);

        // Simulas que el usuario existe
        when(usuarioRepositorio.findById(Id)).thenReturn(Optional.of(usuario));

        // Simulas que no falla al borrar conversiones
        doNothing().when(conversionRepositorio).deleteByUsuario(usuario);

        // Simulas que no falla al borrar usuario
        doNothing().when(usuarioRepositorio).delete(usuario);

        // Ahora llamas al método de servicio
        usuarioServicio.eliminar(Id);

        // Verificaciones:
        verify(usuarioRepositorio, times(1)).findById(Id);
        verify(conversionRepositorio, times(1)).deleteByUsuario(usuario);
        verify(usuarioRepositorio, times(1)).delete(usuario);
    }

    // test de los query y metodos personalizados
    @Test
    public void testBuscarPorNombre() {
        String nombre = "elpepe";
        List<Usuario> usuariosMock = List.of(crearUsuario());
        when(usuarioRepositorio.buscarPorNombre(nombre)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("elpepe", usuarios.get(0).getNombreUsuario());
    }

    @Test
    public void testBuscarPorCorreo() {
        String correo = "pepito@gmail.com";
        List<Usuario> usuariosMock = List.of(crearUsuario());
        when(usuarioRepositorio.buscarExactoPorCorreo(correo)).thenReturn(usuariosMock);
        List<Usuario> usuarios = usuarioServicio.buscarPorCorreo(correo);
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("pepito@gmail.com", usuarios.get(0).getCorreoElectronico());
    }

    @Test
    public void findByCorreoElectronicoAndNombreUsuario() {
        Usuario usuario = crearUsuario();
        List<Usuario> usuariosMock = List.of(usuario);

        when(usuarioRepositorio.findByCorreoElectronicoAndNombreUsuario(usuario.getCorreoElectronico(), usuario.getNombreUsuario())).thenReturn(usuariosMock);

        List<Usuario> actualizado = usuarioServicio.findByCorreoElectronicoAndNombreUsuario(usuario.getCorreoElectronico(), usuario.getNombreUsuario());

        assertNotNull(actualizado);
        assertEquals(1, actualizado.size());
        assertEquals(usuario.getCorreoElectronico(), actualizado.get(0).getCorreoElectronico());
        assertEquals(usuario.getNombreUsuario(), actualizado.get(0).getNombreUsuario());
        verify(usuarioRepositorio, times(1)).findByCorreoElectronicoAndNombreUsuario(usuario.getCorreoElectronico(), usuario.getNombreUsuario());
    }
}
