package com.ajitech.teloconvierto.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Conversion;



@Repository
public interface ConversionRepositorio extends JpaRepository<Conversion, Integer> {
    List<Conversion> findByUsuarioId(Integer usuarioId);

    @Query("SELECT c FROM Conversion c WHERE c.formatoOrigen.id = :formatoId")
    List<Conversion> buscarPorFormatoOrigen(@Param("formatoId") Integer formatoId);

    @Query("SELECT c FROM Conversion c WHERE c.formatoConvertido.id = :formatoId")
    List<Conversion> buscarPorFormatoConvertido(@Param("formatoId") Integer formatoId);

}