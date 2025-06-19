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

    // Crear 10 usuarios 
    for (int i = 0; i < 10; i++) {
        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico(faker.internet().emailAddress());  // Correo electrónico aleatorio
        usuario.setNombreUsuario(faker.internet().username());  // Nombre de usuario aleatorio
        usuario.setClave(faker.internet().password(8, 16)); // Contraseña aleatoria
        usuarioRepositorio.save(usuario);   // Guarda el usuario
    }
    List<Usuario> usuarios = usuarioRepositorio.findAll();


    // Formatos ficticios
    String[] extensiones = {"pdf", "docx", "xlsx", "jpg", "png", "txt"};
    for (String ext : extensiones) {
        Formato formato = new Formato();
        formato.setNombreFormato(faker.file().mimeType());  // Nombre de formato aleatorio (tipo MIME)
        formato.setExtensionFormato(ext);   // Extensión del formato
        formatoRepositorio.save(formato);   // Guarda el formato
    }
    // Obtiene la lista de todos los formatos creados
    List<Formato> formatos = formatoRepositorio.findAll();

    // Crear 20 archivos y asignarles un formato aleatorio
    //var formatos = formatoRepositorio.findAll();
    for (int i = 0; i < 20; i++) {
    Archivo archivo = new Archivo();
    String nombreArchivo = faker.file().fileName();
    // Limita a 30 caracteres
    if (nombreArchivo.length() > 30) {
        nombreArchivo = nombreArchivo.substring(0, 30);
    }
    System.out.println("Guardando archivo con nombre: [" + nombreArchivo + "] y longitud: " + nombreArchivo.length());
    archivo.setNombreArchivo(nombreArchivo); // Usa el nombre limitado
    archivo.setFormato(formatos.get(random.nextInt(formatos.size())));
    archivoRepositorio.save(archivo);
}
    

    // Crear 15 conversiones 
    //var usuarios = usuarioRepositorio.findAll();
    for (int i = 0; i < 15; i++) {
        Conversion conversion = new Conversion();
        // Asigna un usuario aleatorio a la conversión
        conversion.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
        // Selecciona un formato de origen aleatorio
        Formato formatoOrigen = formatos.get(random.nextInt(formatos.size()));
        Formato formatoConvertido;
        // Asegura que el formato convertido sea diferente al de origen
        //if (indexConvertido == indexOrigen) {
        //       indexConvertido = (indexConvertido + 1) % formatos.size();
        //   }
        // Selecciona un formato convertido diferente al de origen
        do {
            formatoConvertido = formatos.get(random.nextInt(formatos.size()));
        } while (formatoConvertido.getId().equals(formatoOrigen.getId()));
        conversion.setFormatoOrigen(formatoOrigen);             // Asigna formato de origen
        conversion.setFormatoConvertido(formatoConvertido);     // Asigna formato convertido
        conversionRepositorio.save(conversion);                 // Guarda la conversión
    }

    } // fin del método run
} // fin de la clase DataLoader
