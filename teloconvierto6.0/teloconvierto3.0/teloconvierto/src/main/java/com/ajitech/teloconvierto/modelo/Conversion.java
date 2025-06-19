package com.ajitech.teloconvierto.modelo;

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
@Table(name="conversion")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Conversion {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name= "usuario_id", nullable = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name ="formato_origen_id", nullable = false)
    private Formato formatoOrigen;

    @ManyToOne
    @JoinColumn(name = "formato_convertido_id",nullable = false)
    private Formato formatoConvertido;

}
