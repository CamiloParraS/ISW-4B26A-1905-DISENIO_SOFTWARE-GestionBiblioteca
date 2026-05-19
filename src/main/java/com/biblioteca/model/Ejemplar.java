package com.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ejemplares")
public class Ejemplar {

    @Id
    private String id;

    // Este atributo no esta en el diagrama, pero es necesario para relacionar el ejemplar con su
    // libro
    private String libroId; // Referencia al Libro (catálogo)

    private String codigoEjemplar; // Código único para cada ejemplar
    private String estado; // Disponible, Prestado, Reservado
    private String ubicacion;
}
