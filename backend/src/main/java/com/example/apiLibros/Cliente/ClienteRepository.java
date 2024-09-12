package com.example.apiLibros.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    Cliente findByUsername(String username);
    //como el nombre indica, asi como busco por id, puedo buscar por nombre de usuario
}
