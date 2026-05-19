package com.biblioteca.service.impl;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Prestamo;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
            EjemplarRepository ejemplarRepository, UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {
        validarSolicitudPrestamo(request);

        usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con id: " + request.getUsuarioId()));

        Ejemplar ejemplar = ejemplarRepository.findById(request.getEjemplarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ejemplar no encontrado con id: " + request.getEjemplarId()));

        if (!"DISPONIBLE".equals(ejemplar.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El ejemplar no está disponible para préstamo");
        }

        ejemplar.setEstado("PRESTADO");
        ejemplarRepository.save(ejemplar);

        Prestamo prestamo = new Prestamo();
        prestamo.setIdUsuario(request.getUsuarioId());
        prestamo.setEjemplarId(request.getEjemplarId());

        LocalDate now = LocalDate.now();

        prestamo.setFechaPrestamo(now.format(DATE_FORMATTER));
        prestamo.setFechaDevolucionEsperada(now.plusDays(14).format(DATE_FORMATTER));
        prestamo.setEstado("ACTIVO");

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        return mapToResponse(prestamoGuardado);
    }

    @Override
    public PrestamoResponse consultarPrestamo(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prestamo no encontrado con id: " + id));

        return mapToResponse(prestamo);
    }

    @Override
    public PrestamoResponse registrarDevolucion(String idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prestamo no encontrado con id: " + idPrestamo));

        if ("DEVUELTO".equals(prestamo.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El préstamo ya fue devuelto");
        }

        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ejemplar no encontrado con id: " + prestamo.getEjemplarId()));

        ejemplar.setEstado("DISPONIBLE");
        ejemplarRepository.save(ejemplar);

        LocalDate now = LocalDate.now();

        prestamo.setFechaDevolucionReal(now.format(DATE_FORMATTER));
        prestamo.setEstado("DEVUELTO");

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);

        return mapToResponse(prestamoActualizado);
    }

    @Override
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll().stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PrestamoResponse mapToResponse(Prestamo prestamo) {
        return new PrestamoResponse(prestamo.getId(), prestamo.getIdUsuario(),
                prestamo.getEjemplarId(), prestamo.getEstado(), prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucionEsperada(), prestamo.getFechaDevolucionReal());
    }

    private void validarSolicitudPrestamo(PrestamoRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La solicitud de préstamo no puede ser nula");
        }

        if (request.getUsuarioId() == null || request.getUsuarioId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El id del usuario es obligatorio");
        }

        if (request.getEjemplarId() == null || request.getEjemplarId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El id del ejemplar es obligatorio");
        }
    }
}
