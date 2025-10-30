package edu.empresa.impl;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.entities.EstudianteCarreraId;
import edu.empresa.factories.DAOFactory;
import edu.empresa.repositories.EstudianteCarreraRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class EstudianteCarreraImpl implements EstudianteCarreraRepository {
    private final EntityManager em;
    public static volatile EstudianteCarreraImpl instance;

    private EstudianteCarreraImpl() {
        this.em = DAOFactory.getInstance().getEntityManager();
    }

    public static EstudianteCarreraImpl getInstance() {
        if (instance == null) {
            synchronized (EstudianteRepositoryImpl.class) {
                if (instance == null) {
                    instance = new EstudianteCarreraImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void anotarEstudiante(Estudiante estu, Carrera c, int inscripcion, int graduacion, int antiguedad) {
        try {
            em.getTransaction().begin();
            Estudiante nuevo = em.find(Estudiante.class, estu.getDni());
            if (nuevo != null) {
                Carrera car = em.find(Carrera.class, c.getIdCarrera());
                if (car != null) {
                    // Verificar si ya existe la relación
                    EstudianteCarreraId id = new EstudianteCarreraId(nuevo.getDni(), car.getIdCarrera());
                    EstudianteCarrera existente = em.find(EstudianteCarrera.class, id);

                    if (existente == null) {
                        EstudianteCarrera ec = new EstudianteCarrera(nuevo, car, inscripcion, graduacion, antiguedad);
                        em.persist(ec);
                        em.getTransaction().commit();
                    } else {
                        em.getTransaction().rollback();
                        System.out.println("La relación entre el estudiante " + nuevo.getDni() +
                                " y la carrera " + car.getIdCarrera() + " ya existe. Se omite la inserción.");
                    }
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
