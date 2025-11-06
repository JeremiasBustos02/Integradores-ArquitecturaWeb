package edu.empresa.controller;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.GenerarReporteDTO;
import edu.empresa.model.Carrera;
import edu.empresa.service.CarreraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("carreras")
@Api(value = "Carrera Controller")
public class CarreraController {
    private final CarreraService service;
    @Autowired
    public CarreraController(CarreraService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "Obtener todas las carreras")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Carreras encontradas correctamente"),
            @ApiResponse(code = 500, message = "Error interno en el servidor")
    })
    public ResponseEntity<List<CarreraDTO>> getCarreras(@RequestParam(required = false) Boolean inscriptos) {
        try {
            if (inscriptos != null && inscriptos) {
                return ResponseEntity.status(HttpStatus.OK).body(service.getCarrerasConInscriptos());
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(service.getCarreras());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/reporte")
    @ApiOperation(value = "Generar reporte de carreras con inscriptos y egresados por año",
                  notes = "Genera un reporte completo que muestra para cada carrera la cantidad de " +
                          "inscriptos y egresados por año. Las carreras están ordenadas alfabéticamente " +
                          "y los años de manera cronológica.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reporte generado correctamente"),
            @ApiResponse(code = 500, message = "Error interno en el servidor")
    })
    public ResponseEntity<List<GenerarReporteDTO>> generarReporte() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.generarReporteCarreras());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
