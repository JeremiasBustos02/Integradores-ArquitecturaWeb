package edu.empresa.controller;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.service.EstudianteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint para dar de alta un nuevo estudiante
     * @param estudianteDTO DTO con los datos del estudiante a crear
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/alta")
    @ApiOperation(value = "Dar de alta un nuevo estudiante",
                  notes = "Crea un nuevo estudiante en el sistema con todos sus datos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Estudiante creado exitosamente"),
        @ApiResponse(code = 400, message = "Datos inválidos en la solicitud"),
        @ApiResponse(code = 409, message = "El estudiante ya existe (DNI o LU duplicado)"),
        @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<?> altaEstudiante(@Valid @RequestBody EstudianteDTO estudianteDTO) {
        try {
            EstudianteDTO resultado = service.altaEstudiante(estudianteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            // Manejar errores específicos del negocio
            if (e.getMessage().contains("Ya existe")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + e.getMessage());
        }
    }
}
