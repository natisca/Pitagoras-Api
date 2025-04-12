package com.example.piatgorasapi.servlet;


import com.example.piatgorasapi.dao.Inscripcion;
import com.example.piatgorasapi.service.InscripcionService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet(name = "InscripcionServlet", urlPatterns = {"/inscripciones", "/inscripciones/*"})
public class InscripcionServlet extends HttpServlet {
    private final InscripcionService service = new InscripcionService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Inscripcion i = gson.fromJson(req.getReader(), Inscripcion.class);
        resp.setContentType("application/json");
        if (service.registrar(i)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Inscripción registrada\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"No se pudo registrar\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path != null && path.startsWith("/carrera")) {
            String nombre = req.getParameter("nombre");
            var resultado = service.buscarPorCarrera(nombre);
            resp.getWriter().write(gson.toJson(resultado));
        } else if (path != null && path.equals("/priorizadas")) {
            var resultado = service.priorizadas();
            resp.getWriter().write(gson.toJson(resultado));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta no encontrada\"}");
        }
    }
}


