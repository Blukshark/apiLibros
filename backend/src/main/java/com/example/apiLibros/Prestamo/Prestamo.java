package com.example.apiLibros.Prestamo;

import com.example.apiLibros.Cliente.Cliente;
import com.example.apiLibros.Libro.Libro;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_prestamo;

    @ManyToOne
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_inicio")
    private LocalDate fecha_inicio;

    @Column(name = "fecha_fin")
    private LocalDate fecha_fin;

    // Constructores
    public Prestamo() {}

    public Prestamo(Libro libro, Cliente cliente, LocalDate fecha_inicio, LocalDate fecha_fin) {
        this.libro = libro;
        this.cliente = cliente;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    // Getters y Setters
    public Long getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(Long id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
}


