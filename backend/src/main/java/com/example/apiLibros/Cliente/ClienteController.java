package com.example.apiLibros.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los clientes
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Obtener un cliente segun su id
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    // Crear un nuevo cliente
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cli) {
        return clienteRepository.save(cli);
    }

    // Actualizar un cliente existente
    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cli) {
        Cliente existingCliente = clienteRepository.findById(id).orElse(null);
        if (existingCliente != null) {
            existingCliente.setNombre(cli.getNombre());
            existingCliente.setApellido(cli.getApellido());
            existingCliente.setUser(cli.getUser());
            existingCliente.setPass(cli.getPass());
            return clienteRepository.save(existingCliente);
        }
        return null;
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public String deleteCliente(@PathVariable Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return "Cliente eliminado con éxito.";
        }
        return "Cliente no encontrado.";
    }

}
