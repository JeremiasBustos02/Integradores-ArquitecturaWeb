package edu.empresa.controller;

import edu.empresa.service.CarreraService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("carreras")
@Api(value = "Carrera Controller", description =  "REST API para gestionar las carreras")
public class CarreraController {
    @Qualifier("carreraService")
    @Autowired
    private final CarreraService service;

    public CarreraController(@Qualifier("carreraService") CarreraService service) {
        this.service = service;
    }
}
