package com.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prestamos")
public class Prestamo {
    @Id
    private String id;
    private String fechaPrestamo;
    private String fechaDevolucionEsperada;
    private String fechaDevolucionReal;
    private String estado;

    // Estos atributos no están en el diagrama, pero son necesarios para relacionar el préstamo con
    // el usuario y el ejemplar
    private String idUsuario;
    private String ejemplarId;

}
