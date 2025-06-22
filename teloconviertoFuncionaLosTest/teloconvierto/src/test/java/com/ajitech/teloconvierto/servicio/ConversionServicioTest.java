package com.ajitech.teloconvierto.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void testObtemerPorId(){
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
    public void testEliminarPorId(){
        Conversion conversion =  crearConversion();
        when(conversionRepositorio.findById(1)).thenReturn(Optional.of(conversion));

        conversionServicio.eliminar(1);
        // Verificar que se haya llamado al método delete del repositorio
        verify(conversionRepositorio, times(1)).delete(conversion);
    }

    @Test
    public void testPatchActualizar(){
        
    }

}