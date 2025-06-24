package com.ajitech.teloconvierto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// import modelos
import com.ajitech.teloconvierto.modelo.Archivo;
import com.ajitech.teloconvierto.modelo.Formato;
import com.ajitech.teloconvierto.modelo.Usuario;
import com.ajitech.teloconvierto.modelo.Conversion;

//  import repositorios
import com.ajitech.teloconvierto.repositorio.ArchivoRepositorio;
import com.ajitech.teloconvierto.repositorio.ConversionRepositorio;
import com.ajitech.teloconvierto.repositorio.FormatoRepositorio;
import com.ajitech.teloconvierto.repositorio.UsuarioRepositorio;

import net.datafaker.Faker;
import java.util.Random;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    private ArchivoRepositorio archivoRepositorio;

    @Autowired
    private ConversionRepositorio conversionRepositorio;

    @Autowired
    private FormatoRepositorio formatoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public void run(String... args) throws Exception {
    // Instancia Faker para generar datos aleatorios
    Faker faker = new Faker();
    Random random = new Random();

    //Usuarios
    for(int i = 0; i < 5; i++){
        Usuario usuario = new Usuario();
        usuario.setId(i+1);
        usuario.setCorreoElectronico(faker.internet().emailAddress());
        usuario.setNombreUsuario(faker.internet().username());
        usuario.setClave(faker.internet().password(8, 20));
        usuarioRepositorio.save(usuario);
    }
    List<Usuario> usuarios = usuarioRepositorio.findAll();


    //Formatos
    
    for (int i = 0; i < 5; i++) {
        Formato formato = new Formato();
        formato.setId(i+1);
        formato.setNombreFormato(faker.file().mimeType());
        formato.setExtensionFormato(faker.file().extension());
        formatoRepositorio.save(formato);
    }
    // Obtiene la lista de todos los formatos creados
    List<Formato> formatos = formatoRepositorio.findAll();

    //Archivos
    for(int i = 0; i < 5; i++){
        Archivo archivo = new Archivo();
        archivo.setId(i+1);
        archivo.setNombreArchivo(faker.lorem().word().replaceAll("[^a-zA-Z0-9]", "") + "_" + faker.number().randomDigit());
        archivo.setFormato(formatos.get(random.nextInt(formatos.size())));
        archivoRepositorio.save(archivo);
    }
    List<Archivo> archivos = archivoRepositorio.findAll();

     for (int i = 0; i < 5; i++) {
        Conversion conversion = new Conversion();
        // El usuario no es obligatorio
        if (!usuarios.isEmpty() && faker.bool().bool()) {
            conversion.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
        } else {
            conversion.setUsuario(null);
        }
        conversion.setArchivoOrigen(archivos.get(random.nextInt(archivos.size())));
        conversion.setFormatoConvertido(formatos.get(random.nextInt(formatos.size())));
        conversionRepositorio.save(conversion);
    }

    } // fin del método run
} // fin de la clase DataLoader
