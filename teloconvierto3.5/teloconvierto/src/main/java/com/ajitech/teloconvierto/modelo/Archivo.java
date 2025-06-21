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
    
    @Column(nullable = false, length = 255)
    private String nombreArchivo;

    @ManyToOne
    @JoinColumn(name="idFormato", nullable = false)
    private Formato formato; 
}
