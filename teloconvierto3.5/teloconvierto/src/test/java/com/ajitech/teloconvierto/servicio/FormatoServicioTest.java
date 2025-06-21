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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Formato;

import com.ajitech.teloconvierto.repositorio.FormatoRepositorio;

@SpringBootTest
public class FormatoServicioTest {

    @Autowired
    private FormatoServicio formatoServicio;

    @MockBean
    private FormatoRepositorio formatoRepositorio;

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
    public void testObtenerPorId() {
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
    }

    @Test
    public void testPatcActualizar() {
        Formato existing = crearFormato();
        Formato patchData = new Formato();
        patchData.setNombreFormato("DOCX");
        patchData.setExtensionFormato(".docx");

        when(formatoRepositorio.findById(1)).thenReturn(java.util.Optional.of(existing));
        when(formatoRepositorio.save(any(Formato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Formato result = formatoServicio.patchFormato(1, patchData);

        assertNotNull(result);
        assertEquals("DOCX", result.getNombreFormato());
        assertEquals(".docx", result.getExtensionFormato());
        verify(formatoRepositorio, times(1)).save(any(Formato.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer Id = 1;
        doNothing().when(formatoRepositorio).deleteById(Id);;
        formatoServicio.eliminar(Id);
        verify(formatoRepositorio, times(1)).deleteById(Id);
    }

    @Test
    public void testBuscarPorNombre() {
        String nombre = "PDF";
        List<Formato> formatosMock = List.of(crearFormato());
        when(formatoRepositorio.buscarPorNombre(nombre)).thenReturn(formatosMock);

        List<Formato> formatos = formatoServicio.buscarPorNombre(nombre);
        assertNotNull(formatos);
        assertEquals(1, formatos.size());
        assertEquals("PDF", formatos.get(0).getNombreFormato());
    }

    @Test
    public void testBuscarPorExtension() {
        String extension = ".pdf";
        List<Formato> formatosMock = List.of(crearFormato());
        when(formatoRepositorio.buscarPorExtension(extension)).thenReturn(formatosMock);

        List<Formato> Formatos = formatoServicio.buscarPorExtension(extension);
        assertNotNull(Formatos);
        assertEquals(1, Formatos.size());
        assertEquals(".pdf", Formatos.get(0).getExtensionFormato());
    }

    // QUERY TESTS

}
