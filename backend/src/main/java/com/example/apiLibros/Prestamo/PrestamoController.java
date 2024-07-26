package com.example.apiLibros.Prestamo;

import com.example.apiLibros.Libro.Libro;
import com.example.apiLibros.Libro.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private LibroRepository libroRepository; //Para gestionar el stock al hacer prestamos

    // Obtener todos los préstamos
    @GetMapping
    public List<Prestamo> getAllPrestamos() {
        return prestamoRepository.findAll();
    }

    // Obtener préstamo por id
    @GetMapping("/{id}")
    public Prestamo getPrestamoById(@PathVariable Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    // Crear nuevo préstamo
    @PostMapping
    public Prestamo createPrestamo(@RequestBody Prestamo prest) {
        Libro libro = prest.getLibro();
        if (libro != null) {
            if (libro.getStock() > 0) {
                libro.setStock(libro.getStock() - 1); // Reducir stock
                libroRepository.save(libro); // Guardar libro actualizado
                return prestamoRepository.save(prest); // Guardar préstamo
            } else {
                throw new RuntimeException("No hay stock disponible para el libro.");
            }
        } else {
            throw new RuntimeException("Libro no encontrado.");
        }
    }

    // Actualizar un préstamo existente
    @PutMapping("/{id}")
    public Prestamo updatePrestamo(@PathVariable Long id, @RequestBody Prestamo prest) {
        Prestamo existingPrestamo = prestamoRepository.findById(id).orElse(null);
        if (existingPrestamo != null) {
            existingPrestamo.setLibro(prest.getLibro());
            existingPrestamo.setCliente(prest.getCliente());
            existingPrestamo.setFecha_inicio(prest.getFecha_inicio());
            existingPrestamo.setFecha_fin(prest.getFecha_fin());
            return prestamoRepository.save(existingPrestamo);
        }
        return null;
    }

    // Eliminar préstamo por ID
    @DeleteMapping("/{id}")
    public String deletePrestamo(@PathVariable Long id) {
        if (prestamoRepository.existsById(id)) {
            Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
            if (prestamo != null && prestamo.getLibro() != null) {
                Libro libro = prestamo.getLibro();
                libro.setStock(libro.getStock() + 1); // Reponer stock si se elimina el préstamo
                libroRepository.save(libro);
            }
            prestamoRepository.deleteById(id);
            return "Préstamo eliminado con éxito.";
        } else {
            return "Préstamo no encontrado.";
        }
    }
}
