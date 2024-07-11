package com.example.apiLibros.Libro;

import jakarta.persistence.*;

import java.util.Date;

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

    @Column(name = "numPaginas") // columna: numPaginas
    private Integer numPaginas;

    @Column(name = "sinopsis") // sinopsis
    private String sinopsis;

    @Column(name = "fechaPublicacion") // columna fechaPublicacion
    private Date fechaPublicacion;

    //constructores
    public Libro() { //sin parametros
        //this.id_autor = 0L;
        this.titulo = "";
        this.genero = "";
        this.numPaginas = 0;
        this.sinopsis = "";
        this.fechaPublicacion = null;
    }
    //con parametros
    public Libro(Long id_autor, String titulo, String genero, Integer numPaginas, String sinopsis, Date fechaPublicacion) {
        this.id_autor = id_autor;
        this.titulo = titulo;
        this.genero = genero;
        this.numPaginas = numPaginas;
        this.sinopsis = sinopsis;
        this.fechaPublicacion = fechaPublicacion;
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

    public Integer getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(Integer numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

}
