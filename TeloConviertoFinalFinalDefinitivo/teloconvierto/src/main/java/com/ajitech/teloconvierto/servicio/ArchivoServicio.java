package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;

import jakarta.transaction.Transactional;

import java.util.List;


import javax.management.RuntimeErrorException;

@Service
@Transactional
public class ArchivoServicio {
    
    @Autowired
    private ArchivoRepositorio archivoRepositorio;
    @Autowired
    private ConversionRepositorio conversionRepositorio;
    @Autowired
    private ConversionServicio conversionServicio;

    public List<Archivo> listar() {
        return archivoRepositorio.findAll();
    }

    public Archivo obtenerPorId(Integer id) {
        return archivoRepositorio.findById(id).orElse(null);
    }

    public Archivo guardar(Archivo archivo) {
        return archivoRepositorio.save(archivo);
    }

    // Actualizar todo el archivo
    public Archivo actualizarTodo(Integer id, Archivo archivo) {
        Archivo existingArchivo = archivoRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeErrorException(new Error("Archivo no encontrado")));
        
        if (archivo.getNombreArchivo() != null) existingArchivo.setNombreArchivo(archivo.getNombreArchivo());
        if (archivo.getFormato() != null) existingArchivo.setFormato(archivo.getFormato());
        return archivoRepositorio.save(existingArchivo);
    }

    public void eliminarr(Integer id) {
        Archivo archivo = archivoRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeErrorException(new Error("Archivo no encontrado")));
        List<Conversion> conversiones = conversionRepositorio.findByArchivoOrigen(archivo);
        for(Conversion conversion : conversiones){
            conversionServicio.eliminar(conversion.getId());
        }
        archivoRepositorio.delete(archivo);
    }

    public Archivo patchArchivo(Integer id, Archivo archivo){
        Archivo existingArchivo = obtenerPorId(id);
        if (existingArchivo != null) {
            if(archivo.getNombreArchivo() != null){
                existingArchivo.setNombreArchivo(archivo.getNombreArchivo());
            }
            if(archivo.getFormato() != null){
                existingArchivo.setFormato(archivo.getFormato());
            }
            return guardar(existingArchivo);
        }
        return null;
    }

// Metodos del @Query

    public List<Archivo> buscarPorNombreYExtension(String nombre,String extension) {
    return archivoRepositorio.buscarPorNombreYExtension(nombre, extension);
    }

    public List<Archivo> buscarPorFormato(Integer formatoId) {
    return archivoRepositorio.buscarPorFormato(formatoId);
    }

    public List<Archivo> findByFormatoIdAndNombreArchivo(Integer formatoId, String nombreArchivo) {
        return archivoRepositorio.findByFormatoIdAndNombreArchivo(formatoId, nombreArchivo);
    }
}
