package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajitech.teloconvierto.modelo.Formato;

import com.ajitech.teloconvierto.repositorio.FormatoRepositorio;

import java.util.List;
import java.util.Optional;


@Service
public class FormatoServicio {
    @Autowired
    private FormatoRepositorio formatoRepositorio;

    public Formato guardar(Formato formato) {
        return formatoRepositorio.save(formato);
    }

    public Formato actualizar(Formato formato) {
        return formatoRepositorio.save(formato);
    }

    public void eliminar(Integer id) {
        formatoRepositorio.deleteById(id);
    }

    public Optional<Formato> obtenerPorId(Integer id) {
        return formatoRepositorio.findById(id);
    }

    public List<Formato> listar() {
        return formatoRepositorio.findAll();
    }
}
    



