package com.example.apiLibros.Autor;

import jakarta.persistence.*;

@Entity
@Table (name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutor;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    //Constructor sin parametros
    public Autor()
    {
        //this.idAutor = 0L;
        this.nombre = "";
        this.apellido = "";
    }
    //Constructor con parametros
    public Autor(Long idAutor, String nombre, String apellido) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
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
