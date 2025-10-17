package edu.empresa.controller;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.service.EstudianteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("estudiantes")
@Api(value = "Estudiante Controller", description = "REST API para gestionar estudiantes")
public class EstudianteController {
    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @GetMapping("/")
    @ApiOperation(value = "Encontrar todos los estudiantes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Todos los estudiantes encontrados correctamente"),
            @ApiResponse(code = 500, message = "Error interno en el servidor")
    })
    public ResponseEntity<List<EstudianteDTO>> getEstudiantes() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getEstudiantes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
