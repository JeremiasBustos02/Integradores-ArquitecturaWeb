package edu.empresa.controller;

import edu.empresa.dto.EstudianteCarreraDTO;
import edu.empresa.dto.EstudianteDTO;
import edu.empresa.dto.InscripcionRequestDTO;
import edu.empresa.service.EstudianteCarreraService;
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
@RequestMapping("inscripciones")
@Api(value = "InscripcionController", description = "REST API para gestionar inscripciones de estudiantes en carreras")
public class EstudianteCarreraController {

    private final EstudianteCarreraService service;

    @Autowired
    public EstudianteCarreraController(EstudianteCarreraService service) {
        this.service = service;
    }

    /**
     * Endpoint simplificado para inscribir un estudiante en una carrera
     *
     * @param request DTO simplificado con datos básicos de inscripción
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping
    @ApiOperation(value = "Inscribir estudiante en carrera",
            notes = "Crea una nueva inscripción de un estudiante en una carrera específica")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Estudiante inscrito exitosamente"),
            @ApiResponse(code = 400, message = "Datos inválidos en la solicitud"),
            @ApiResponse(code = 404, message = "Estudiante o carrera no encontrados"),
            @ApiResponse(code = 409, message = "El estudiante ya está inscrito en esta carrera"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<?> inscribirEstudiante(@Valid @RequestBody InscripcionRequestDTO request) {
        try {
            // Convertir a EstudianteCarreraDTO completo
            EstudianteCarreraDTO dto = new EstudianteCarreraDTO();
            dto.setIdEstudiante(request.getIdEstudiante());
            dto.setIdCarrera(request.getIdCarrera());
            dto.setInscripcion(0);
            dto.setGraduacion(0);
            dto.setAntiguedad(0);

            // Llamar al servicio para realizar la inscripción
            EstudianteCarreraDTO resultado = service.inscribirEstudianteEnCarrera(dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(resultado);

        } catch (RuntimeException e) {
            // Manejar errores específicos del negocio
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + e.getMessage());
            } else if (e.getMessage().contains("ya está inscrito")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Error: " + e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error interno: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener estudiantes de una carrera filtrados por ciudad
     *
     * @param idCarrera ID de la carrera (opcional)
     * @param ciudad    ciudad de residencia (opcional)
     * @return ResponseEntity con la lista de estudiantes
     */
    @GetMapping
    @ApiOperation(value = "Obtener estudiantes inscriptos con filtros",
            notes = "Recupera estudiantes inscritos con filtros opcionales por carrera y ciudad, " +
                    "ordenados por apellido y nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estudiantes encontrados correctamente"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public ResponseEntity<List<EstudianteDTO>> getEstudiantesInscritos(
            @RequestParam(required = false) Integer carrera,
            @RequestParam(required = false) String ciudad) {
        try {
            // Si ambos parámetros están presentes, usar el filtro completo
            if (carrera != null && ciudad != null) {
                List<EstudianteDTO> estudiantes = service.getEstudiantesByCarreraAndCiudad(carrera, ciudad);
                return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
            } else {
                // Para otros casos, retornar lista vacía o implementar otros filtros
                return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
