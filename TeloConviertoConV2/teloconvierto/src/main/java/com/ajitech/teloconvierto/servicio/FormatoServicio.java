package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;
import com.ajitech.teloconvierto.repositorio.FormatoRepositorio;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class FormatoServicio {
    @Autowired
    private FormatoRepositorio formatoRepositorio;
    @Autowired
    private ArchivoRepositorio archivoRepositorio;
    @Autowired
    private ConversionRepositorio conversionRepositorio;
   
    public List<Formato> listar() {
        return formatoRepositorio.findAll();
    }

    public Formato obtenerPorId(Integer id) {
        return formatoRepositorio.findById(id).orElse(null);
    }

    public Formato guardar(Formato formato) {
        return formatoRepositorio.save(formato);
    }

    // Actualizar todo el formato
    public Formato actualizarTodo(Integer id, Formato formato) {
        Formato existingFormato = formatoRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Formato no encontrado"));

        if (formato.getNombreFormato() != null) existingFormato.setNombreFormato(formato.getNombreFormato());
        if (formato.getExtensionFormato() != null) existingFormato.setExtensionFormato(formato.getExtensionFormato());
        return formatoRepositorio.save(existingFormato);
    }

    public void eliminar(Integer id) {
         // Busca el formato, si no existe lanza error
        Formato formato = formatoRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Formato no encontrado"));

        // Busca archivos relacionados a ese formato
        List<Archivo> archivos = archivoRepositorio.findByFormato(formato);

        // Recorre los archivos y borra conversiones y luego los archivos
        for (Archivo archivo : archivos) {
            conversionRepositorio.deleteByArchivoOrigen(archivo);
            archivoRepositorio.delete(archivo);
        }

        // Finalmente elimina el formato
        formatoRepositorio.delete(formato);
    }

    public Formato patchFormato(Integer id, Formato formato){
        Formato existingFormato = obtenerPorId(id);
        if (existingFormato != null) {
            if(formato.getNombreFormato() != null){
                existingFormato.setNombreFormato(formato.getNombreFormato());
            }
            if(formato.getExtensionFormato() != null){
                existingFormato.setExtensionFormato(formato.getExtensionFormato());
            }
            return guardar(existingFormato);
        }
        return null;
    }
//Metodos del repositorio @Query
    public List<Formato> buscarPorNombre(String nombre) {
        return formatoRepositorio.buscarPorNombre(nombre);
    }

    public List<Formato> buscarPorExtension(String extension) {
        return formatoRepositorio.buscarPorExtension(extension);
    }

    public List<Formato> findByNombreFormatoAndExtensionFormato(String nombre, String extension) {
        return formatoRepositorio.findByNombreFormatoAndExtensionFormato(nombre, extension);
    }

}
    



