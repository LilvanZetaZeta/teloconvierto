package com.ajitech.teloconvierto.modelo;

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

public class Formato {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable=false, length=30)
    private String nombreFormato;

    @Column(nullable=false, length=30)
    private String extensionFormato;
}
