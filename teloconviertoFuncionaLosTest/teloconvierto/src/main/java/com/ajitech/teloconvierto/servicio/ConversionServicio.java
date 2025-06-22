package com.ajitech.teloconvierto.servicio;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConversionServicio {
    @Autowired
    private ConversionRepositorio conversionRepositorio;

    public List<Conversion> listar() {
        return conversionRepositorio.findAll();
    }

    public Conversion obtenerPorId(Integer id) {
        return conversionRepositorio.findById(id).orElse(null);
    }

    public Conversion guardar(Conversion conversion) {
        return conversionRepositorio.save(conversion);
    }

    public void eliminar(Integer id) {
        Conversion conversion = conversionRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Conversion no encontrada"));
        conversionRepositorio.delete(conversion);
    }

    public Conversion patchConversion(Integer id, Conversion conversion){
        Conversion existingConversion = obtenerPorId(id);
        if (existingConversion != null){
            if (conversion.getUsuario() != null ){
                existingConversion.setUsuario(conversion.getUsuario());
            }
            if (conversion.getArchivoOrigen() != null ){
                existingConversion.setArchivoOrigen(conversion.getArchivoOrigen());
            }
            if (conversion.getFormatoConvertido() != null ){
                existingConversion.setFormatoConvertido(conversion.getFormatoConvertido());
            }
            return guardar(existingConversion);
        }
        return null;
    }


// Metodos de los @Query

    // metoodo join para buscar por usuario y formato convertido
    public List<Conversion> buscarPorUsuarioYExtensionFormato(String correo, String extensionFormato) {
        return conversionRepositorio.buscarPorUsuarioYExtensionFormato(correo, extensionFormato);
    }
}

