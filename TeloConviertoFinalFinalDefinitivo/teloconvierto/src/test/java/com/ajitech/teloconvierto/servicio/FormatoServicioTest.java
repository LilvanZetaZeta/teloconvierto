package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;
import com.ajitech.teloconvierto.repositorio.FormatoRepositorio;

@SpringBootTest
public class FormatoServicioTest {

    @Autowired
    private FormatoServicio formatoServicio;

    @MockBean
    private FormatoRepositorio formatoRepositorio;

    @MockBean
    private ArchivoRepositorio archivoRepositorio;

    @MockBean
    private ConversionRepositorio conversionRepositorio;

    private Formato crearFormato() {
        return new Formato(1, "PDF", ".pdf");
    }


    @Test
    public void testListar() {
        when(formatoRepositorio.findAll()).thenReturn(List.of(crearFormato()));
        List<Formato> formatos = formatoServicio.listar();
        assertNotNull(formatos);
        assertEquals(1, formatos.size());
    }

     @Test
    public void testListarPorId() {
        Formato formato = crearFormato();
        when(formatoRepositorio.findById(1)).thenReturn(java.util.Optional.of(formato));
        Formato found = formatoServicio.obtenerPorId(1);
        assertNotNull(found);
        assertEquals("PDF", found.getNombreFormato());
    }

    @Test 
    public void testGuardar() {
        Formato formato = crearFormato();
        when(formatoRepositorio.save(formato)).thenReturn(formato);
        Formato saved = formatoServicio.guardar(formato);
        assertNotNull(saved);
        assertEquals(".pdf", saved.getExtensionFormato());
        verify(formatoRepositorio, times(1)).save(any(Formato.class));
    }

    // testActualizarTodo
    @Test
    public void testActualizarTodo() {
        Formato formato = crearFormato();
        Formato nuevo = new Formato();
        nuevo.setId(1);  // id existente
        nuevo.setNombreFormato("DOCX");
        nuevo.setExtensionFormato(".docx");

        Mockito.when(formatoRepositorio.findById(1)).thenReturn(Optional.of(formato));
        Mockito.when(formatoRepositorio.save(any(Formato.class))).thenReturn(nuevo);

        Formato actualizado = formatoServicio.actualizarTodo(1, formato);

        assertNotNull(actualizado);
        assertEquals(1, actualizado.getId());

        assertEquals("DOCX", actualizado.getNombreFormato());
        assertEquals(".docx", actualizado.getExtensionFormato());
        verify(formatoRepositorio, times(1)).save(any(Formato.class));
    }

    @Test
    public void testPatchActualizar() {
        Formato existingFormato = crearFormato();
        Formato patchData = new Formato();
        patchData.setNombreFormato("DOCX");
        patchData.setExtensionFormato(".docx");

        when(formatoRepositorio.findById(1)).thenReturn(java.util.Optional.of(existingFormato));
        when(formatoRepositorio.save(any(Formato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Formato actualizado = formatoServicio.patchFormato(1, patchData);

        assertNotNull(actualizado);
        assertEquals("DOCX", actualizado.getNombreFormato());
        assertEquals(".docx", actualizado.getExtensionFormato());
        
        verify(formatoRepositorio, times(1)).save(any(Formato.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer id = 1;
        Formato formato = crearFormato();
        formato.setId(id);
        // Creamos archivos relacionados a ese formato
        Archivo archivo1 = new Archivo();
        archivo1.setId(10);
        archivo1.setFormato(formato);
        // Simula que findById devuelve el formato
        when(formatoRepositorio.findById(id)).thenReturn(Optional.of(formato));
        // Simula que findByFormato devuelve los archivos relacionados
        when(archivoRepositorio.findByFormato(formato)).thenReturn(List.of(archivo1));
        // Simula que los deletes no hacen nada
        doNothing().when(conversionRepositorio).deleteByArchivoOrigen(any(Archivo.class));
        doNothing().when(archivoRepositorio).delete(any(Archivo.class));
        doNothing().when(formatoRepositorio).delete(formato);
        // Ejecuta el método a probar
        formatoServicio.eliminarr(id);
        // Verifica que se buscaron el formato y los archivos
        verify(formatoRepositorio, times(1)).findById(id);
        verify(archivoRepositorio, times(1)).findByFormato(formato);
        // Verifica que se eliminaron conversiones y archivos para cada archivo
        verify(conversionRepositorio, times(1)).deleteByArchivoOrigen(archivo1);
        verify(archivoRepositorio, times(1)).delete(archivo1);
        // Verifica que se eliminó el formato al final
        verify(formatoRepositorio, times(1)).delete(formato);
    }


    // BUSQUEDA TESTS que usan @Query
    @Test
    public void testBuscarPorNombreFormato() {
        String nombre = "PDF";
        List<Formato> formatosMock = List.of(crearFormato());
        when(formatoRepositorio.buscarPorNombre(nombre)).thenReturn(formatosMock);

        List<Formato> formatos = formatoServicio.buscarPorNombre(nombre);
        assertNotNull(formatos);
        assertEquals(1, formatos.size());
        assertEquals("PDF", formatos.get(0).getNombreFormato());
    }

    @Test
    public void testBuscarPorExtensionFormato() {
        String extension = ".pdf";
        List<Formato> formatosMock = List.of(crearFormato());
        when(formatoRepositorio.buscarPorExtension(extension)).thenReturn(formatosMock);

        List<Formato> Formatos = formatoServicio.buscarPorExtension(extension);
        assertNotNull(Formatos);
        assertEquals(1, Formatos.size());
        assertEquals(".pdf", Formatos.get(0).getExtensionFormato());
    }

    @Test
    public void findByNombreFormatoAndExtensionFormato() {
        Formato formato = crearFormato();
        List<Formato> formatosMock = List.of(formato);

        when(formatoRepositorio.findByNombreFormatoAndExtensionFormato(formato.getNombreFormato(), formato.getExtensionFormato())).thenReturn(formatosMock);
        List<Formato> actualizado = formatoServicio.findByNombreFormatoAndExtensionFormato(formato.getNombreFormato(), formato.getExtensionFormato());
        
        assertNotNull(actualizado);
        assertEquals(1, actualizado.size());
        assertEquals(formato.getNombreFormato(), actualizado.get(0).getNombreFormato());
        assertEquals(formato.getExtensionFormato(), actualizado.get(0).getExtensionFormato());
        verify(formatoRepositorio, times(1)).findByNombreFormatoAndExtensionFormato(formato.getNombreFormato(), formato.getExtensionFormato());
    }

}
