package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;

@SpringBootTest
public class ConversionServicioTest {

    @Autowired
    private ConversionServicio conversionServicio;

    @MockBean
    private ConversionRepositorio conversionRepositorio;

    private Conversion crearConversion() {
        Conversion conversion = new Conversion();
        conversion.setId(1);
        conversion.setUsuario(new Usuario(1, "pepito@gmail.com", "elpepe", "123"));
        conversion.setArchivoOrigen(new Archivo(1, "prueba", new Formato(1, "PDF", ".pdf")));
        conversion.setFormatoConvertido(new Formato(1, "TXT", ".txt"));
        return conversion;
    }

    @Test
    public void testListar(){
        when(conversionRepositorio.findAll()).thenReturn(List.of(crearConversion()));
        List<Conversion> conversiones = conversionServicio.listar();
        assertNotNull(conversiones);
        assertEquals(1, conversiones.size());
    }

    @Test
    public void testObtenerPorId(){
        Conversion conversion =  crearConversion();
        when(conversionRepositorio.findById(1)).thenReturn(Optional.of(conversion));

        Conversion encontrada = conversionServicio.obtenerPorId(1);
        assertNotNull(encontrada);
        assertEquals(1, encontrada.getId());
    }

    @Test
    public void testGuardar(){
        Conversion conversion = crearConversion();
        when(conversionRepositorio.save(conversion)).thenReturn(conversion);

        Conversion guardada = conversionServicio.guardar(conversion);
        assertNotNull(guardada);
        assertEquals(1, guardada.getId());

    }

    @Test
    public void testActualizarTodo(){
        Conversion conversion = crearConversion();
        Conversion nuevo =  new Conversion();
        nuevo.setId(1);
        nuevo.setUsuario(new Usuario(2, "nuevo@mail.com", "nuevoUser", "456"));
        nuevo.setArchivoOrigen(new Archivo(2, "nuevoArchivo", new Formato(3, "XLSX", ".xlsx")));
        nuevo.setFormatoConvertido(new Formato(2, "DOCX", ".docx"));

        // Simulamos que findById(1) devuelve la conversión original
        Mockito.when(conversionRepositorio.findById(1)).thenReturn(Optional.of(conversion));
        // Simulamos que save devuelve la conversión actualizada
        Mockito.when(conversionRepositorio.save(Mockito.any(Conversion.class))).thenReturn(nuevo);

        // Ejecutamos el método a probar
        Conversion actualizada = conversionServicio.actualizarTodo(1, nuevo);

        // Verificamos que el id no cambió
        assertNotNull(actualizada);
        assertEquals(1, actualizada.getId());

        
        // Verificamos sí cambiaron
        assertEquals(2, actualizada.getUsuario().getId());
        assertEquals("nuevoUser", actualizada.getUsuario().getNombreUsuario());
        assertEquals(2, actualizada.getArchivoOrigen().getId());
        assertEquals(3, actualizada.getArchivoOrigen().getFormato().getId());
        assertEquals(2, actualizada.getFormatoConvertido().getId());
    }

     @Test
    public void testPatchActualizar(){
        Conversion existingConversion = crearConversion();
        Conversion patchData = new Conversion();
        patchData.setFormatoConvertido(new Formato(2, "DOCX", ".docx"));


        when(conversionRepositorio.findById(1)).thenReturn(Optional.of(existingConversion));
        when(conversionRepositorio.save(patchData)).thenReturn(patchData);

        Conversion actualizado = conversionServicio.patchConversion(1, patchData);
        assertNotNull(actualizado);
        assertEquals("DOCX", actualizado.getFormatoConvertido().getNombreFormato());
        verify(conversionRepositorio, times(1)).save(patchData);
        
    }

    @Test
    public void testEliminarPorId(){
        Conversion conversion =  crearConversion();
        when(conversionRepositorio.findById(1)).thenReturn(Optional.of(conversion));

        conversionServicio.eliminar(1);
        // Verificar que se haya llamado al método delete del repositorio
        verify(conversionRepositorio, times(1)).delete(conversion);
    }

    // test de query y personalizado

    @Test
    public void buscarPorUsuarioYExtensionFormato(){
        Conversion conversion = crearConversion();
        List<Conversion> conversionesMock = List.of(conversion);

        when(conversionRepositorio.buscarPorUsuarioYExtensionFormato(conversion.getUsuario().getCorreoElectronico(), conversion.getFormatoConvertido().getNombreFormato())).thenReturn(conversionesMock);

        List<Conversion> actualizada = conversionServicio.buscarPorUsuarioYExtensionFormato(conversion.getUsuario().getCorreoElectronico(), conversion.getFormatoConvertido().getNombreFormato());
        assertNotNull(actualizada);

        assertEquals(1, actualizada.size());
        assertEquals(conversion.getUsuario().getCorreoElectronico(), actualizada.get(0).getUsuario().getCorreoElectronico());
        assertEquals(conversion.getFormatoConvertido().getNombreFormato(), actualizada.get(0).getFormatoConvertido().getNombreFormato());
        verify(conversionRepositorio, times(1)).buscarPorUsuarioYExtensionFormato(conversion.getUsuario().getCorreoElectronico(), conversion.getFormatoConvertido().getNombreFormato());

    }

    @Test
    public void buscarPorFormatoConvertido(){
        Integer formatoId = 1;

        List<Conversion> conversionesMock = List.of(crearConversion());

        when(conversionRepositorio.buscarPorFormatoConvertido(formatoId)).thenReturn(conversionesMock);

        List<Conversion> conversiones = conversionServicio.buscarPorFormatoConvertido(formatoId);
        assertNotNull(conversiones);
        assertEquals(1, conversiones.size());
        assertEquals(1, conversiones.get(0).getFormatoConvertido().getId());
    }

    @Test
    public void findByUsuarioIdAndArchivoOrigenId(){
        Integer usuarioId = 1;
        Integer archivoId = 1;

        List<Conversion> conversionesMock = List.of(crearConversion());

        when(conversionRepositorio.findByUsuarioIdAndArchivoOrigenId(usuarioId, archivoId)).thenReturn(conversionesMock);

        List<Conversion> conversiones = conversionServicio.findByUsuarioIdAndArchivoOrigenId(usuarioId, archivoId);
        assertNotNull(conversiones);
        assertEquals(1, conversiones.size());
        assertEquals(usuarioId, conversiones.get(0).getUsuario().getId());
        assertEquals(archivoId, conversiones.get(0).getArchivoOrigen().getId());
        verify(conversionRepositorio, times(1)).findByUsuarioIdAndArchivoOrigenId(usuarioId, archivoId);
    }



   

}