package com.example.apiLibros.Cliente;

import jakarta.persistence.*;

@Entity
@Table(name="cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "user")
    private String user;

    @Column(name = "pass")
    private String pass;

    // Constructor sin parametros
    public Cliente() {}
    // Constructor con parametros
    public Cliente(String nombre, String apellido, String user, String pass) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.user = user;
        this.pass = pass;
    }

    // Getters y Setters
    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
