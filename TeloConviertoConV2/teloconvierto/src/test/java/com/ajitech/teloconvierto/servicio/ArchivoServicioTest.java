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
import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;

@SpringBootTest
public class ArchivoServicioTest {

    @Autowired
    private ArchivoServicio archivoServicio;
    
    @MockBean
    private ConversionRepositorio conversionRepositorio;
    
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
    public void testActualizarTodo(){
        // Creamos el archivo existente
        Archivo archivo = crearArchivo();

        // Creamos el archivo nuevo con los datos a actualizar (excepto el id)
        Archivo nuevo = new Archivo();
        nuevo.setId(1); // El id debe ser igual al existente
        nuevo.setNombreArchivo("Torpedo");
        nuevo.setFormato(new Formato(2, "DOCX", ".docx"));

        // Simulamos que findById(1) devuelve el archivo existente
        Mockito.when(archivoRepositorio.findById(1)).thenReturn(Optional.of(archivo));
        // Simulamos que save devuelve el archivo actualizado
        Mockito.when(archivoRepositorio.save(any(Archivo.class))).thenReturn(nuevo);

        // Ejecutamos el método a probar
        Archivo actualizado = archivoServicio.actualizarTodo(1, nuevo);

        // Verificamos que el id no cambió
        assertNotNull(actualizado);
        assertEquals(1, actualizado.getId());
        
        // Verificamos que los demás campos sí cambiaron
        assertEquals("Torpedo", actualizado.getNombreArchivo());
        assertEquals(2, actualizado.getFormato().getId());
        assertEquals("DOCX", actualizado.getFormato().getNombreFormato());
        assertEquals(".docx", actualizado.getFormato().getExtensionFormato());
    }

    @Test
    public void testPatchActualizar(){
        Archivo existingArchivo = crearArchivo();
        Archivo patchData =  new Archivo();
        patchData.setNombreArchivo("Torpedo");
        patchData.setFormato(new Formato(2,"Docx", ".docx"));

        when(archivoRepositorio.findById(1)).thenReturn(Optional.of(existingArchivo));
        when(archivoRepositorio.save(any(Archivo.class))).thenReturn(patchData);
        
        Archivo actualizado = archivoServicio.patchArchivo(1, patchData);
        assertNotNull(actualizado);
        assertEquals("Torpedo", actualizado.getNombreArchivo());
        assertEquals("Docx", actualizado.getFormato().getNombreFormato());
        verify(archivoRepositorio, times(1)).save(any(Archivo.class));
    }

    @Test
    public void testEliminarPorId() {
        Integer id = 1;
        Archivo archivo = new Archivo();
        archivo.setId(id);

        // Creamos una conversión asociada a este archivo
        Conversion conversion1 = new Conversion();
        conversion1.setId(100);
        conversion1.setArchivoOrigen(archivo);

        // Simula que findById devuelve el archivo
        when(archivoRepositorio.findById(id)).thenReturn(Optional.of(archivo));
        // Simula que findByArchivoOrigen devuelve solo una conversión
        when(conversionRepositorio.findByArchivoOrigen(archivo)).thenReturn(List.of(conversion1));

        // Simula que los deletes no hacen nada
        doNothing().when(conversionRepositorio).delete(conversion1);
        doNothing().when(archivoRepositorio).delete(archivo);

        // Ejecuta el método a probar
        archivoServicio.eliminar(id);

        // Verifica que se buscaron el archivo y las conversiones
        verify(archivoRepositorio, times(1)).findById(id);
        verify(conversionRepositorio, times(1)).findByArchivoOrigen(archivo);

        // Verifica que se eliminó la conversión asociada
        verify(conversionRepositorio, times(1)).delete(conversion1);

        // Verifica que se eliminó el archivo al final
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

        @Test
        public void findByFormatoIdAndNombreArchivo(){
            Archivo archivo = crearArchivo();
            List<Archivo> archivosMock = List.of(archivo);

            when(archivoRepositorio.findByFormatoIdAndNombreArchivo(archivo.getFormato().getId(), archivo.getNombreArchivo())).thenReturn(archivosMock);
        
            List<Archivo> actualizado = archivoServicio.findByFormatoIdAndNombreArchivo(archivo.getFormato().getId(), archivo.getNombreArchivo());

            assertNotNull(actualizado);
            assertEquals(1, actualizado.size());    
            assertEquals(archivo.getNombreArchivo(), actualizado.get(0).getNombreArchivo());
            assertEquals(archivo.getFormato().getId(), actualizado.get(0).getFormato().getId());
            verify(archivoRepositorio, times(1)).findByFormatoIdAndNombreArchivo(archivo.getFormato().getId(), archivo.getNombreArchivo());
        }
}
