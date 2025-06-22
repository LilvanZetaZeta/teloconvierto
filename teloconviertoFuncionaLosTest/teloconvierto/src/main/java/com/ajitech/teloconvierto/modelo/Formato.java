package com.ajitech.teloconvierto.modelo;

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
@Table(name="formato")
@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "modelo que representa un formato de archivo")  
public class Formato {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable=false, length=30)
    private String nombreFormato;

    @Column(nullable=false, length=30)
    private String extensionFormato;
}
