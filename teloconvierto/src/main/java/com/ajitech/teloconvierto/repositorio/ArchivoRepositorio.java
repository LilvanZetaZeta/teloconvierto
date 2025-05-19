package com.ajitech.teloconvierto.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Archivo;


@Repository
public interface ArchivoRepositorio extends JpaRepository<Archivo, Integer> {
 @Query("SELECT a FROM Archivo a WHERE a.formato.id = :formatoId")
    List<Archivo> buscarPorFormato(@Param("formatoId") Integer formatoId);

}
