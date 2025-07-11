package com.ajitech.teloconvierto.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Formato;

@Repository
public interface FormatoRepositorio extends JpaRepository<Formato, Integer> {


    @Query("SELECT f FROM Formato f WHERE f.nombreFormato LIKE %:nombre%")
    List<Formato> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT f FROM Formato f WHERE f.extensionFormato = :extension")
    List<Formato> buscarPorExtension(@Param("extension") String extension);

    List<Formato> findByNombreFormatoAndExtensionFormato(String nombreFormato, String extensionFormato);
}

