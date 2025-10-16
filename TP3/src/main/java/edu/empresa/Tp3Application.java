package edu.empresa;

import edu.empresa.utils.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Tp3Application {
    @Autowired
    private CSVReader csv;
    @PostConstruct
    public void init() throws IOException {
        csv.cargarDatos();

    }
    public static void main(String[] args) {
        SpringApplication.run(Tp3Application.class, args);
    }

}
