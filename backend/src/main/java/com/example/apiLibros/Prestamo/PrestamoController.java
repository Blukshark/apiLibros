package com.example.apiLibros.Prestamo;

import com.example.apiLibros.Cliente.Cliente;
import com.example.apiLibros.Cliente.ClienteRepository;
import com.example.apiLibros.Libro.Libro;
import com.example.apiLibros.Libro.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepo;
    @Autowired
    private LibroRepository libroRepo; //Para gestionar el stock al hacer prestamos
    @Autowired
    private ClienteRepository clienteRepo; //Para gestionar el stock al hacer prestamos

    // Obtener todos los préstamos
    @GetMapping
    public List<Prestamo> getAllPrestamos() {
        return prestamoRepo.findAll();
    }

    // Obtener préstamo por id
    @GetMapping("/{id}")
    public Prestamo getPrestamoById(@PathVariable Long id) {
        return prestamoRepo.findById(id).orElse(null);
    }

    // Crear nuevo préstamo
    @PostMapping
    public ResponseEntity<String> createPrestamo(@RequestBody Prestamo prest) {
        Optional<Libro> libroOpt = libroRepo.findById(prest.getLibro().getId_libro());
        Optional<Cliente> clienteOpt = clienteRepo.findById(prest.getCliente().getId_cliente());

        if (libroOpt.isPresent() && clienteOpt.isPresent()) {
            Libro lib = libroOpt.get();
            Cliente cli = clienteOpt.get();

            if (lib.getStock() > 0) {
                lib.setStock(lib.getStock() - 1); // Reducir stock
                libroRepo.save(lib); // Guardar libro actualizado

                prest.setLibro(lib);
                prest.setCliente(cli);
                prestamoRepo.save(prest); // Guardar el préstamo recien creado
                return ResponseEntity.status(HttpStatus.CREATED).body("Préstamo creado con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay stock disponible para el libro.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Revisar el id de cliente y/o libro.");
        }
    }

    // Actualizar un préstamo existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePrestamo(@PathVariable Long id, @RequestBody Prestamo prest) {
        Optional<Prestamo> existingPrestamoOpt = prestamoRepo.findById(id);

        if (existingPrestamoOpt.isPresent()) {
            Prestamo existingPrestamo = existingPrestamoOpt.get();

            if (prest.getLibro() == null ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El libro no puede ser nulo.");
            }
            else if (prest.getCliente() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El cliente no puede ser nulo.");
            }

            Optional<Libro> libroOpt = libroRepo.findById(prest.getLibro().getId_libro());
            Optional<Cliente> clienteOpt = clienteRepo.findById(prest.getCliente().getId_cliente());

            if (libroOpt.isPresent() ) {
                if(clienteOpt.isPresent())
                {
                    Libro lib = libroOpt.get();
                    Cliente cli = clienteOpt.get();

                    existingPrestamo.setLibro(lib);
                    existingPrestamo.setCliente(cli);
                    existingPrestamo.setFecha_inicio(prest.getFecha_inicio());
                    existingPrestamo.setFecha_fin(prest.getFecha_fin());
                    prestamoRepo.save(existingPrestamo);
                    return ResponseEntity.ok("Préstamo actualizado con éxito.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente no encontrado.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Libro no encontrado.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado.");
        }
    }




    // Eliminar préstamo por ID
    @DeleteMapping("/{id}")
    public String deletePrestamo(@PathVariable Long id) {
        if (prestamoRepo.existsById(id)) {
            Prestamo prestamo = prestamoRepo.findById(id).orElse(null);
            if (prestamo != null && prestamo.getLibro() != null) {
                Libro libro = prestamo.getLibro();
                libro.setStock(libro.getStock() + 1); // Reponer stock si se elimina el préstamo
                libroRepo.save(libro);
            }
            prestamoRepo.deleteById(id);
            return "Préstamo eliminado con éxito.";
        } else {
            return "Préstamo no encontrado.";
        }
    }
}
