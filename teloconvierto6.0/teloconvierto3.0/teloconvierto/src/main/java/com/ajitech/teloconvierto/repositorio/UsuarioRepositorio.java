package com.ajitech.teloconvierto.repositorio;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajitech.teloconvierto.modelo.Usuario;



@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario LIKE %:nombre%")
    List<Usuario> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.correoElectronico = :correo")
    List<Usuario> buscarExactoPorCorreo(@Param("correo") String correo);
}