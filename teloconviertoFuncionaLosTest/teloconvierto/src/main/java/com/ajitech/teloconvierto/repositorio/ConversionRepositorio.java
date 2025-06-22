package com.ajitech.teloconvierto.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Conversion;
import com.ajitech.teloconvierto.modelo.Usuario;

@Repository
public interface ConversionRepositorio extends JpaRepository<Conversion, Integer> {
    List<Conversion> findByUsuarioId(Integer usuarioId);

    @Query("""
        SELECT c FROM Conversion c
        JOIN c.usuario u
        JOIN c.formatoConvertido f
        WHERE u.correoElectronico = :correo
        AND f.extensionFormato LIKE %:extensionFormato%
        """)
    List<Conversion> buscarPorUsuarioYExtensionFormato(@Param("correo") String correo,@Param("extensionFormato") String nombreFormato);

    @Query("SELECT c FROM Conversion c WHERE c.formatoConvertido.id = :formatoId")
    List<Conversion> buscarPorFormatoConvertido(@Param("formatoId") Integer formatoId);
    
    List<Conversion> findByUsuarioIdAndArchivoOrigenId(Integer usuarioId, Integer archivoId);


    List<Conversion> findByArchivoOrigen(Archivo archivo);

    List<Conversion> findByUsuario(Usuario usuario);

    // Método para eliminar conversiones por archivo origen y usuario
    void deleteByArchivoOrigen(Archivo archivo);
    void deleteByUsuario(Usuario usuario);

}