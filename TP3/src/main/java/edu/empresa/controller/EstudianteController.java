package edu.empresa.controller;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.service.EstudianteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public EstudianteController(EstudianteService service) {
        this.service = service;
    }


    /**
     * Endpoint para traer todos los estudiantes por orden alfabetico de apellido
     *
     * @return ResponseEntity con el resultado de la operación
     */
    @GetMapping
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
     *
     * @param estudianteDTO DTO con los datos del estudiante a crear
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping
    @ApiOperation(value = "Crear un nuevo estudiante",
            notes = "Crea un nuevo estudiante en el sistema con todos sus datos")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Estudiante creado exitosamente"),
            @ApiResponse(code = 400, message = "Datos inválidos en la solicitud"),
            @ApiResponse(code = 409, message = "El estudiante ya existe (DNI o LU duplicado)"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<?> crearEstudiante(@Valid @RequestBody EstudianteDTO estudianteDTO) {
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

    /**
     * Endpoint para buscar un estudiante por su número de libreta universitaria (LU)
     *
     * @param lu número de libreta universitaria
     * @return ResponseEntity con el estudiante encontrado o error
     */
    @GetMapping("/lu/{lu}")
    @ApiOperation(value = "Buscar estudiante por número de libreta universitaria (LU)",
            notes = "Recupera un estudiante específico usando su número de libreta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estudiante encontrado correctamente"),
            @ApiResponse(code = 404, message = "No se encontró estudiante con ese número de libreta"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<?> getEstudianteByLU(@PathVariable int lu) {
        try {
            EstudianteDTO estudiante = service.getEstudianteByLU(lu);
            return ResponseEntity.status(HttpStatus.OK).body(estudiante);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No se encontró")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    /**
     * Endpoint para buscar estudiantes por género
     *
     * @param genero género a buscar (Male/Female)
     * @return ResponseEntity con la lista de estudiantes del género especificado
     */
    @GetMapping("/genero/{genero}")
    @ApiOperation(value = "Buscar estudiantes por género",
            notes = "Recupera todos los estudiantes de un género específico, ordenados por apellido y nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estudiantes encontrados correctamente"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<?> getEstudiantesByGenero(@PathVariable String genero) {
        try {
            List<EstudianteDTO> estudiantes = service.getEstudiantesByGenero(genero);
            return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }
}
