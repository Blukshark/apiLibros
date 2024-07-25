package com.example.apiLibros.Autor;

import jakarta.persistence.*;

@Entity
@Table (name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_autor;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    //Constructor sin parametros
    public Autor()
    {
        this.nombre = "";
        this.apellido = "";
    }
    //Constructor con parametros
    public Autor(Long id_autor, String nombre, String apellido) {
        this.id_autor = id_autor;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getId_autor() {
        return id_autor;
    }

    public void setId_autor(Long id_autor) {
        this.id_autor = id_autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
