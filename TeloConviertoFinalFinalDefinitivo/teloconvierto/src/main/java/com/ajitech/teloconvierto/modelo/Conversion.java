package com.ajitech.teloconvierto.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="conversion")
@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Modelo que representa una conversión de archivo")  
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @ManyToOne
    @NotNull(message = "El archivo de origen es obligatorio") // msg de Validación de campo obligatorio    
    @JoinColumn(name = "archivo_origen_id", nullable = false)
    private Archivo archivoOrigen;

    @ManyToOne
    @NotNull(message = "El formato de origen es obligatorio") // msg de Validación de campo obligatorio
    @JoinColumn(name = "formato_convertido_id", nullable = false)
    private Formato formatoConvertido;
}
