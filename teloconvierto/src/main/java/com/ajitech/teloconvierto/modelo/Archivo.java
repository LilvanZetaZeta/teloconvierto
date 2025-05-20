package com.ajitech.teloconvierto.modelo;

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



public class Archivo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 30)
    private String nombreArchivo;

    @ManyToOne
    @JoinColumn(name="idFormato", nullable = false)
    private Formato formato;

  
}
