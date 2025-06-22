package com.ajitech.teloconvierto.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;


@Repository
public interface ArchivoRepositorio extends JpaRepository<Archivo, Integer> {
  @Query("""
    SELECT a FROM Archivo a
    JOIN a.formato f
    WHERE a.nombreArchivo LIKE %:nombre%
      AND f.extensionFormato = :extension
    """)
    List<Archivo> buscarPorNombreYExtension(@Param("nombre") String nombre, @Param("extension") String extension);

  @Query("SELECT a FROM Archivo a WHERE a.formato.id = :formatoId")
    List<Archivo> buscarPorFormato(@Param("formatoId") Integer formatoId);

    List<Archivo> findByFormatoIdAndNombreArchivo(Integer formatoId, String nombreArchivo);

    List<Archivo> findByFormato(Formato formato);
}
