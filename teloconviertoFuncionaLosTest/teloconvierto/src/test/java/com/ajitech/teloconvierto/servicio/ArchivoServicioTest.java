package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;

@SpringBootTest
public class ArchivoServicioTest {

    @Autowired
    private ArchivoServicio archivoServicio;
    
    @MockBean
    private ArchivoRepositorio archivoRepositorio;

    private Archivo crearArchivo() {
        return new Archivo(1, "prueba", new Formato(1, "PDF", ".pdf"));
    }

    @Test
    public void testListar() {
        when(archivoRepositorio.findAll()).thenReturn(List.of(crearArchivo()));
        List<Archivo> archivos = archivoServicio.listar();
        assertNotNull(archivos);
        assertEquals(1, archivos.size());
    }

    @Test
    public void testListarPorId() {
        Archivo archivo = crearArchivo();
        when(archivoRepositorio.findById(1)).thenReturn(java.util.Optional.of(archivo));
        Archivo foundaArchivo = archivoServicio.obtenerPorId(1); // revisar
        assertNotNull(foundaArchivo);
        assertEquals("prueba", foundaArchivo.getNombreArchivo());
    }

    @Test
    public void testGuardar() {
        Archivo archivo = crearArchivo();
        when(archivoRepositorio.save(archivo)).thenReturn(archivo);

        Archivo savedArchivo = archivoServicio.guardar(archivo);
        assertNotNull(savedArchivo);
        assertEquals(archivo, savedArchivo);
    }

    @Test
    public void testPatcActualizar(){
        Archivo existe = crearArchivo();
        Archivo nuevo =  new Archivo();
        nuevo.setNombreArchivo("Torpedo");
        nuevo.setFormato(new Formato(2,"Docx", ".docx"));

        when(archivoRepositorio.findById(1)).thenReturn(Optional.of(existe));
        when(archivoRepositorio.save(any(Archivo.class))).thenReturn(existe);
        
        Archivo actualizado = archivoServicio.patchArchivo(1, nuevo);
        assertNotNull(actualizado);
        assertEquals("Torpedo", actualizado.getNombreArchivo());
        assertEquals("Docx", actualizado.getFormato().getNombreFormato());
        verify(archivoRepositorio, times(1)).save(any(Archivo.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer Id = 1;
        Archivo archivo = crearArchivo();
        archivo.setId(Id);

        // Simulas que el archivo existe
        when(archivoRepositorio.findById(Id)).thenReturn(Optional.of(archivo));

        // Ejecuta la eliminación
        archivoServicio.eliminar(Id);

        // Verifica que primero buscó el archivo
        verify(archivoRepositorio, times(1)).findById(Id);

        // Verifica que eliminó el archivo
        verify(archivoRepositorio, times(1)).delete(archivo);

        }

        @Test
        public void testBuscarPorNombre(){
            String nombre = "prueba";
            String nExtension = ".pdf";
            
            List<Archivo> archivosMock =  List.of(crearArchivo());
            when(archivoRepositorio.buscarPorNombreYExtension(nombre, nExtension)).thenReturn(archivosMock);

            List<Archivo> archivos = archivoServicio.buscarPorNombreYExtension(nombre, nExtension);
            assertNotNull(archivos);
            assertEquals(1, archivos.size());
            assertEquals("prueba", archivos.get(0).getNombreArchivo());
        }

        @Test
        public void testBuscarPorFormato(){
            Integer formatoId = 1;

            List<Archivo> archivosmock = List.of(crearArchivo());
            when(archivoRepositorio.buscarPorFormato(formatoId)).thenReturn(archivosmock);

            List<Archivo> archivos =  archivoServicio.buscarPorFormato(formatoId);
            assertNotNull(archivos);
            assertEquals(1, archivos.size());
            assertEquals("prueba", archivos.get(0).getNombreArchivo());
        }
}
