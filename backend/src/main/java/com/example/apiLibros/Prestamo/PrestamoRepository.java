package com.example.apiLibros.Prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo,Long> {
    // Consulta personalizada para buscar prestamos por id_cliente
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id_cliente = :clienteId")
    List<Prestamo> findByClienteId(@Param("clienteId") Long clienteId);
}
