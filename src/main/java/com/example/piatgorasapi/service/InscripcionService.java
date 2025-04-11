package com.example.piatgorasapi.service;

import com.example.piatgorasapi.dao.Inscripcion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InscripcionService {
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public boolean registrar(Inscripcion i) {
        // Validar no más de 7 inscripciones por estudiante
        long count = inscripciones.stream()
                .filter(x -> x.getDocumento().equals(i.getDocumento()))
                .count();
        if (count >= 7) return false;

        // Validar que no exista ya inscripción con misma asignatura y estudiante
        boolean repetida = inscripciones.stream()
                .anyMatch(x -> x.getDocumento().equals(i.getDocumento()) && x.getAsignatura().equals(i.getAsignatura()));
        if (repetida) return false;

        i.setPrioridad(calcularPrioridad(i));
        inscripciones.add(i);
        return true;
    }

    public List<Inscripcion> buscarPorCarrera(String carrera) {
        return inscripciones.stream()
                .filter(x -> x.getCarrera().equalsIgnoreCase(carrera))
                .toList();
    }

    public List<Inscripcion> priorizadas() {
        return inscripciones.stream()
                .sorted(Comparator.comparingInt(i -> -i.getPrioridad())) // mayor prioridad primero
                .toList();
    }

    private int calcularPrioridad(Inscripcion i) {
        double pesoProm = i.getPromedioAcumulado() * 0.6;
        double pesoCreditos = (i.getCreditos() / 10.0) * 0.3;
        long dias = ChronoUnit.DAYS.between(LocalDate.parse(i.getFechaInscripcion()), LocalDate.now());
        double pesoTiempo = dias * 0.1;
        return (int) Math.round(pesoProm + pesoCreditos + pesoTiempo);
    }
}




