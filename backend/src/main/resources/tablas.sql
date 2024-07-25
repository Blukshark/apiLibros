CREATE TABLE "autor"(
    "id_autor" SERIAL PRIMARY KEY,
    "nombre" VARCHAR(100) NOT NULL,
    "apellido" VARCHAR(100) NOT NULL
);

CREATE TABLE "libro"(
    "id_libro" SERIAL PRIMARY KEY,
    "id_autor" SMALLINT NOT NULL REFERENCES "autor"("id_autor") ON DELETE CASCADE ON UPDATE CASCADE,
    "titulo" VARCHAR(255) NOT NULL,
    "genero" VARCHAR(100) NOT NULL,
    "numPaginas" SMALLINT NOT NULL CHECK ("numPaginas" > 0),
    "sinopsis" TEXT NOT NULL,
    "fechaPublicacion" DATE NOT NULL
);

CREATE TABLE "cliente"(
    "id_cliente" SERIAL PRIMARY KEY,
    "nombre" VARCHAR(100) NOT NULL,
    "apellido" VARCHAR(100) NOT NULL
);

CREATE TABLE "prestamo"(
    "id_prestamo" SERIAL PRIMARY KEY,
    "id_libro" SMALLINT NOT NULL REFERENCES "libro"("id_libro"),
    "id_cliente" SMALLINT NOT NULL REFERENCES "cliente"("id_cliente"),
    "fecha_inicio" DATE NOT NULL,
    "fecha_fin" DATE NOT NULL
);