package com.ajitech.teloconvierto.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;



@Service
public class ConversionServicio {
    @Autowired
    private ConversionRepositorio conversionRepositorio;

    public Conversion guardar(Conversion conversion) {
        return conversionRepositorio.save(conversion);
    }

    public Conversion actualizar(Conversion conversion) {
        return conversionRepositorio.save(conversion);
    }

    public void eliminar(Integer id) {
        conversionRepositorio.deleteById(id);
    }

    public Optional<Conversion> obtenerPorId(Integer id) {
        return conversionRepositorio.findById(id);
    }

    public List<Conversion> obtenerPorUsuario(Integer usuarioId) {
        return conversionRepositorio.findByUsuarioId(usuarioId);
    }

    public List<Conversion> listar() {
        return conversionRepositorio.findAll();
    }

    public List<Conversion> buscarPorFormatoOrigen(Integer formatoId) {
    return conversionRepositorio.buscarPorFormatoOrigen(formatoId);
    }

    public List<Conversion> buscarPorFormatoConvertido(Integer formatoId) {
    return conversionRepositorio.buscarPorFormatoConvertido(formatoId);
    }
}

