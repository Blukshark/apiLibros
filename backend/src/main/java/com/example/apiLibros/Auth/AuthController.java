package com.example.apiLibros.Auth;

import com.example.apiLibros.Cliente.Cliente;
import com.example.apiLibros.Cliente.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:8081") // Permitir peticiones desde tu frontend
public class AuthController {
    @Autowired
    private ClienteRepository clienteRepo;

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
