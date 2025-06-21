package com.ajitech.teloconvierto.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Modelo de Usuario")  
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 80)
    private String correoElectronico;

    @Column(nullable = false, unique = true, length = 80)
    private String nombreUsuario;
    
    //No aparece la respuesta de la clave en Json con el metodo GET
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    @Column(nullable = false, length = 40)
    private String clave;
}
