package com.example.apiLibros.Libro;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "libro") // nombre de mi tabla
public class Libro {

    @Id //es un id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_libro; //columna (id_libro)

    @Column (name = "id_autor")
    private Long id_autor; //columna id_autor

    @Column(name = "titulo") // columna: titulo
    private String titulo;

    @Column(name = "genero") // columna: genero
    private String genero;

    @Column(name = "num_paginas") // columna: numPaginas
    private Integer num_paginas;

    @Column(name = "sinopsis") // sinopsis
    private String sinopsis;

    @Column(name = "fecha_publicacion") // columna fechaPublicacion
    private LocalDate fecha_publicacion;

    @Column(name = "stock")
    private Integer stock;

    //constructores
    //sin parametros
    public Libro() { }
    //con parametros
    public Libro(Long id_autor, String titulo, String genero, Integer num_paginas, String sinopsis, LocalDate fecha_publicacion, Integer stock) {
        this.id_autor = id_autor;
        this.titulo = titulo;
        this.genero = genero;
        this.num_paginas = num_paginas;
        this.sinopsis = sinopsis;
        this.fecha_publicacion = fecha_publicacion;
        this.setStock(stock);
    }

    public Long getId_libro() {
        return id_libro;
    }

    public void setId_libro(Long id_libro) {
        this.id_libro = id_libro;
    }

    public Long getId_autor() { return id_autor; }

    public void setId_autor(Long id_autor) { this.id_autor = id_autor; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getNum_paginas() {
        return num_paginas;
    }

    public void setNum_paginas(Integer num_paginas) {
        this.num_paginas = num_paginas;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
