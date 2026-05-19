package com.biblioteca.controller;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;
import com.biblioteca.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // ─────────────────────────────────────────────
    // POST /api/prestamos
    // Crear un nuevo préstamo
    // ─────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@RequestBody PrestamoRequest request) {
        PrestamoResponse response = prestamoService.crearPrestamo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────
    // GET /api/prestamos
    // Listar todos los préstamos
    // ─────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listarPrestamos() {
        List<PrestamoResponse> prestamos = prestamoService.listarPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    // ─────────────────────────────────────────────
    // GET /api/prestamos/{id}
    // Consultar un préstamo por su ID
    // ─────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> consultarPrestamo(@PathVariable String id) {
        PrestamoResponse response = prestamoService.consultarPrestamo(id);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────
    // POST /api/prestamos/{id}/devolucion
    // Registrar la devolución de un préstamo
    // ─────────────────────────────────────────────
    @PostMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> registrarDevolucion(@PathVariable String id) {
        PrestamoResponse response = prestamoService.registrarDevolucion(id);
        return ResponseEntity.ok(response);
    }
}
