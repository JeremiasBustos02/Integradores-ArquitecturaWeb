package edu.empresa.controller;

import edu.empresa.service.EstudianteService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estudiantes")
@Api(value = "Estudiante Controller", description = "REST API para gestionar estudiantes")
public class EstudianteController {
    @Qualifier("estudianteService")
    private final EstudianteService service;

    public EstudianteController(@Qualifier("estudianteService") EstudianteService service) {
        this.service = service;
    }
}
