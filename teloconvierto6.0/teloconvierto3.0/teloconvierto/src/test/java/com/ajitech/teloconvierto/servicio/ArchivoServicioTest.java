package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;

import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;

public class ArchivoServicioTest {

    @Autowired
    private ArchivoServicio archivoServicio;

    
    @MockBean
    private ArchivoRepositorio archivoRepositorio;


    @Test
    public void testListar() {
        when(archivoRepositorio.findAll()).thenReturn(List.of(new Archivo(1, "prueba", new Formato())));
        List<Archivo> archivos = archivoServicio.listar();
        assertNotNull(archivos);
        assertEquals(1, archivos.size());
    }

    @Test
    public void testListarPorId() {
        Archivo archivo = new Archivo(1, "prueba", new Formato());
        when(archivoRepositorio.findById(1)).thenReturn(java.util.Optional.of(archivo));
        Archivo foundarchivo = archivoServicio.obtenerPorId(1).orElse(null); // revisar
        assertNotNull(foundarchivo);
        assertEquals("prueba", foundarchivo.getNombreArchivo());
    }

     @Test 
    public void testGuardar() {
        Archivo archivo = new Archivo(1, "prueba", new Formato());
        when(archivoRepositorio.save(archivo)).thenReturn(archivo);
        Archivo savedarchivo = archivoServicio.guardar(archivo);
        assertNotNull(savedarchivo);
        assertEquals("prueba", savedarchivo.getNombreArchivo());
    }

    @Test
    public void testActualizar() {
        Archivo archivo = new Archivo(1, "prueba", new Formato());
        Archivo patchData = new Archivo();
        when(archivoRepositorio.save(archivo)).thenReturn(archivo);
        patchData.setNombreArchivo("prueba actualizada");

        when(archivoRepositorio.findById(1)).thenReturn(java.util.Optional.of(archivo));
        when(archivoRepositorio.save(any(Archivo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Archivo result = archivoServicio.actualizar(patchData);

        assertNotNull(result);
        assertEquals("prueba", result.getNombreArchivo());
        verify(archivoRepositorio, times(1)).save(archivo);
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        doNothing().when(archivoRepositorio).deleteById(id);
        archivoServicio.eliminar(id);
        verify(archivoRepositorio, times(1)).deleteById(id);
    }

    @Test
    public void testBuscarPorNombre() {
        String nombre = "archivo";
        List<Archivo> archivosMock = List.of(new Archivo(1, "prueba", new Formato()));
        when(archivoRepositorio.buscarPorNombre(nombre)).thenReturn(archivosMock);
        List<Archivo> archivos = archivoServicio.buscarPorNombre(nombre);

        assertNotNull(archivos);
        assertEquals(1, archivos.size());
        assertEquals(1, archivos.size());

    }

    @Test
    public void testBuscarPorFormato() {
        Formato formato = new Formato();
        List<Archivo> archivosMock = List.of(new Archivo(1, "prueba", formato));
        when(archivoRepositorio.buscarPorFormato(formato.getId())).thenReturn(archivosMock);
        List<Archivo> archivos = archivoServicio.buscarPorFormato(formato.getId());

        assertNotNull(archivos);
        assertEquals(1, archivos.size());
        assertEquals("prueba", archivos.get(0).getNombreArchivo());
        verify(archivoRepositorio).buscarPorFormato(formato.getId()); // Verifica que se llame al método con el ID de formato correcto
    }
}
