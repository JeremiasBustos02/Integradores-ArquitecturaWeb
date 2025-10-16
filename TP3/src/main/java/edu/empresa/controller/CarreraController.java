package edu.empresa.controller;

import edu.empresa.model.Carrera;
import edu.empresa.service.CarreraService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carreras/")
@Api(value = "Carrera Controller")
public class CarreraController {
    @Autowired
    private CarreraService service;

    @GetMapping("") public ResponseEntity<?> getCarreras(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getCarreras());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    @GetMapping("carreraInscriptos") public ResponseEntity<?> getCarreraConInscriptos(){
        try {
             return ResponseEntity.status(HttpStatus.OK).body(service.getCarrerasConInscriptos());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");

        }
    }
}
