package edu.empresa.controller;

import edu.empresa.service.EstudianteCarreraService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estudiantesYCarreras")
@Api(value = "estudianteCarreraController", description = "REST API para operaciones con estudiantes y carreras")
public class EstudianteCarreraController {
    @Qualifier("estudianteCarreraService")
    @Autowired
    private final EstudianteCarreraService service;

    public EstudianteCarreraController(@Qualifier("estudianteCarreraService") EstudianteCarreraService service) {
        this.service = service;
    }

}
