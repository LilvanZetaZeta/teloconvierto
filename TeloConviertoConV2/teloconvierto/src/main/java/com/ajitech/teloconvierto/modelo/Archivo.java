package com.ajitech.teloconvierto.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="archivo")
@Data
@AllArgsConstructor
@NoArgsConstructor


@Schema(description = "Modelo que representa un archivo")  
public class Archivo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El nombre del archivo es obligatorio") // msg de Validación de campo obligatorio
    @Column(nullable = false, length = 255)
    private String nombreArchivo;

    @ManyToOne
    @NotNull(message = "El formato del archivo es obligatorio") // msg de Validación de campo obligatorio
    @JoinColumn(name="idFormato", nullable = false)
    private Formato formato; 
}
