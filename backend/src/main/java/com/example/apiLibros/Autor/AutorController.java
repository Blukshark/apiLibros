package com.example.apiLibros.Autor;

import com.example.apiLibros.Libro.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorRepository AutorRepository;

    @GetMapping
    public List<Autor> getAllAutores() {
        return AutorRepository.findAll();
    }
    //obtener autores segun su id
    @GetMapping("/{id}")
    public Autor getAutorById(@PathVariable Long id) {
        return AutorRepository.findById(id).get();
    }
    @PostMapping
    public Autor createAutor(@RequestBody Autor autor) {
        return AutorRepository.save(autor);
    }
    @PutMapping("/{id}")
    public Autor updateAutor(@PathVariable Long id, @RequestBody Autor aut) {
        Autor existingAutor = AutorRepository.findById(id).orElse(null);
        if (existingAutor != null) {
            existingAutor.setNombre(aut.getNombre());
            existingAutor.setApellido(aut.getApellido());
            return AutorRepository.save(existingAutor);
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public String deleteAutor(@PathVariable Long id) {
        if (AutorRepository.existsById(id) ) { //valido que exista
            AutorRepository.deleteById(id); //si existe lo elimino y retorno mensaje
            return "Autor eliminado con éxito.";
        } else { //si no existe no hago nada y muestro mensaje
            return "El autor no existe.";
        }
    }
}
