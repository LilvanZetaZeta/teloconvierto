package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Conversion;
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
    @Autowired
    private ArchivoServicio archivoServicio;
    @Autowired
    private ConversionServicio conversionServicio;
   
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

    public void eliminarr(Integer id){
        Formato formato = formatoRepositorio.findById(id)
            .orElseThrow(()-> new RuntimeException("Formato no encontrado"));
        List<Archivo> archivos = archivoRepositorio.findByFormato(formato);
        for(Archivo archivo : archivos){
            archivoServicio.eliminarr(archivo.getId());
        }
        List<Conversion> conversiones = conversionRepositorio.findByFormatoConvertido(formato);
        for(Conversion conversion : conversiones){
            conversionServicio.eliminar(conversion.getId());
        }
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
    



