package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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


    private Archivo crearArchivo() {
        return new Archivo(1, "prueba", new Formato());
    }

    @Test
    public void testListar() {
        when(archivoRepositorio.findAll()).thenReturn(List.of(crearArchivo()));
        List<Archivo> archivos = archivoServicio.listar();
        assertNotNull(archivos);
        assertEquals(1, archivos.size());
    }

    

    
}
