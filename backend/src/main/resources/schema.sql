CREATE TABLE autor (
    "id_autor" SERIAL PRIMARY KEY,
    "nombre" VARCHAR(100) NOT NULL,
    "apellido" VARCHAR(100) NOT NULL
);
CREATE TABLE cliente (
    "id_cliente" SERIAL PRIMARY KEY,
    "nombre" VARCHAR(100) NOT NULL,
    "apellido" VARCHAR(100) NOT NULL,
    "user" VARCHAR(100) NOT NULL,
    "pass" VARCHAR(255) NOT NULL
);

CREATE TABLE "libro"(
    "id_libro" SERIAL PRIMARY KEY,
    "id_autor" SMALLINT NOT NULL,
    "titulo" VARCHAR(255) NOT NULL,
    "genero" VARCHAR(100) NOT NULL,
    "num_paginas" SMALLINT NOT NULL,
    "sinopsis" TEXT NOT NULL,
    "fecha_publicacion" DATE NOT NULL,
    "stock" Integer NOT NULL DEFAULT 0,
    FOREIGN KEY (id_autor) REFERENCES autor(id_autor)
);

CREATE TABLE "prestamo"(
    "id_prestamo" SERIAL PRIMARY KEY,
    "id_libro" SMALLINT NOT NULL,
    "id_cliente" SMALLINT NOT NULL,
    "fecha_inicio" DATE NOT NULL,
    "fecha_fin" DATE NOT NULL,
    FOREIGN KEY (id_libro) REFERENCES libro(id_libro),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);