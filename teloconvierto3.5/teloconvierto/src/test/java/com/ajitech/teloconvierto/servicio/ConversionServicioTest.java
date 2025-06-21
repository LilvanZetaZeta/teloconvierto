package com.ajitech.teloconvierto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;

@SpringBootTest
public class ConversionServicioTest {

    @Autowired
    private ConversionServicio conversionServicio;

    @MockBean
    private ConversionRepositorio conversionRepositorio;

   


}