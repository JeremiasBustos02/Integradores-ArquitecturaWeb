package edu.empresa.impl;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.repositories.EstudianteCarreraRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class EstudianteCarreraImpl implements EstudianteCarreraRepository {
    EntityManager em;

    public EstudianteCarreraImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void anotarEstudiante(Estudiante estu, Carrera c, int inscripcion, int graduacion, int antiguedad) {
        try {
            em.getTransaction().begin();
            Estudiante nuevo = em.find(Estudiante.class, estu.getDni());
            if (nuevo != null) {
                Carrera car = em.find(Carrera.class, c.getIdCarrera());
                if (car != null) {
                    EstudianteCarrera ec = new EstudianteCarrera(nuevo, car, inscripcion, graduacion, antiguedad);
                    em.persist(ec);
                    em.getTransaction().commit();
                    System.out.println("Estudiante " + estu.getNombre() + " anotado en " + car.getNombre());
                } else {//rollback de carrera
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    System.err.println("Carrera insertada no existe");
                }
            } else {//rollback de estudiante
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error al obtener al estudiante");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.err.println("Error al anotar al estudiante en la carrera");
        }
    }
}
