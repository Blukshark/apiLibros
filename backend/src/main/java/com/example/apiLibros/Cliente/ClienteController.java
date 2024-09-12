package com.example.apiLibros.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepo;

    // Obtener todos los clientes
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepo.findAll();
    }

    // Obtener un cliente segun su id
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteRepo.findById(id).orElse(null);
    }

    // Crear un nuevo cliente
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cli) {
        return clienteRepo.save(cli);
    }

    // Actualizar un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable Long id, @RequestBody Cliente cli) {
        Cliente existingCliente = clienteRepo.findById(id).orElse(null);
        if (existingCliente != null) {
            existingCliente.setNombre(cli.getNombre());
            existingCliente.setApellido(cli.getApellido());
            existingCliente.setUsername(cli.getUsername());
            existingCliente.setPassword(cli.getPassword());
            clienteRepo.save(existingCliente);
            return ResponseEntity.ok("Cliente actualizado con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado.");
        }
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public String deleteCliente(@PathVariable Long id) {
        if (clienteRepo.existsById(id)) {
            clienteRepo.deleteById(id);
            return "Cliente eliminado con éxito.";
        }
        return "Cliente no encontrado.";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Cliente cli) {
        Cliente existingCliente = clienteRepo.findByUsername(cli.getUsername());
        if (existingCliente != null && existingCliente.getPassword().equals(cli.getPassword())) {
            return ResponseEntity.ok("Inicio de sesión exitoso.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
    }

}
