package Libro;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

@Autowired
private LibroRepository libroRepository;

//obtener todos los libros
@GetMapping
public List<Libro> getAllLibros() {
    return libroRepository.findAll();
}

//obtener libros segun su id
@GetMapping("/{id}")
public Libro getLibroById(@PathVariable Long id) {
    return libroRepository.findById(id).get();
}

@PostMapping
public Libro createLibro(@RequestBody Libro lib) {
    return libroRepository.save(lib);
}

@PutMapping("/{id}")
public Libro updateUser(@PathVariable Long id, @RequestBody Libro lib) {
    Libro existingLibro = libroRepository.findById(id).get();
    existingLibro.setId_libro(lib.getId_libro());
    existingLibro.setId_autor(lib.getId_autor());
    existingLibro.setTitulo(lib.getTitulo());
    existingLibro.setGenero(lib.getGenero());
    existingLibro.setNumPaginas(lib.getNumPaginas());
    existingLibro.setSinopsis(lib.getSinopsis());
    existingLibro.setFechaPublicacion(lib.getFechaPublicacion());
    return libroRepository.save(existingLibro);
}

@DeleteMapping("/{id}")
public String deleteUser(@PathVariable Long id) {
    try {
        libroRepository.findById(id).get();
        libroRepository.deleteById(id);
        return "libro eliminado con éxito amigaso.";
    } catch (Exception e) {
        return "El libro no pudo ser encontrado mi rey.";
        }
    }
}





