package com.example.piatgorasapi.service;



import com.example.piatgorasapi.dao.Inscripcion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class InscripcionService {

    private final List<Inscripcion> inscripciones = new ArrayList<>();

    public boolean registrar(Inscripcion i) {
        try {
            long documento = Long.parseLong(i.getDocumento());
            if (documento % 2 != 0) {
                inscripciones.add(i);
                return true;
            }
        } catch (NumberFormatException e) {
            // Documento no válido, retorna false
        }
        return false;
    }

    public List<Inscripcion> buscarPorCarrera(String carrera) {
        return inscripciones.stream()
                .filter(i -> i.getCarrera().equalsIgnoreCase(carrera))
                .collect(Collectors.toList());
    }

    public List<Inscripcion> priorizadas() {
        return inscripciones.stream()
                .sorted((i1, i2) -> Long.compare(
                        diasDesdeInscripcion(i2.getFechaInscripcion()),
                        diasDesdeInscripcion(i1.getFechaInscripcion())))
                .collect(Collectors.toList());
    }

    private long diasDesdeInscripcion(String fechaInscripcion) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInsc = sdf.parse(fechaInscripcion);
            Date fechaActual = new Date();

            long diferenciaMillis = fechaActual.getTime() - fechaInsc.getTime();
            return diferenciaMillis / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            return 0;
        }
    }
}
