package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import java.util.List;
import java.util.Optional;

@Service
public class ArchivoServicio {
    @Autowired
    private ArchivoRepositorio archivoRepositorio;

    public Archivo guardar(Archivo archivo) {
        return archivoRepositorio.save(archivo);
    }

    public Archivo actualizar(Archivo archivo) {
        return archivoRepositorio.save(archivo);
    }

    public void eliminar(Integer id) {
        archivoRepositorio.deleteById(id);
    }

    public Optional<Archivo> obtenerPorId(Integer id) {
        return archivoRepositorio.findById(id);
    }

    public List<Archivo> listar() {
        return archivoRepositorio.findAll();
    }

    public List<Archivo> buscarPorNombre(String nombre) {
    return archivoRepositorio.buscarPorNombre(nombre);
    }

    public List<Archivo> buscarPorFormato(Integer formatoId) {
    return archivoRepositorio.buscarPorFormato(formatoId);
    }
}
