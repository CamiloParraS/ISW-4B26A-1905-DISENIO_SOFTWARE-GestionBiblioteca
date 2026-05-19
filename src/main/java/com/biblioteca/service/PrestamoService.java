package com.biblioteca.service;

import java.util.List;
import com.biblioteca.dto.PrestamoResponse;
import com.biblioteca.dto.PrestamoRequest;

public interface PrestamoService {

    PrestamoResponse crearPrestamo(PrestamoRequest request);

    PrestamoResponse consultarPrestamo(String id);

    PrestamoResponse registrarDevolucion(String idPrestamo);

    List<PrestamoResponse> listarPrestamos();
}
