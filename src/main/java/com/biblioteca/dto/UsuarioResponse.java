package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponse {

    private String id; // ← UsuarioResponse SÍ incluye el id
    private String nombre;
    private String email;

}
