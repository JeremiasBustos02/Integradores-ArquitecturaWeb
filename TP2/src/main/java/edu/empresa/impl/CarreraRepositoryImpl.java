package edu.empresa.impl;

import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.repositories.CarreraRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class CarreraRepositoryImpl implements CarreraRepository {
    EntityManager em;

    public CarreraRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Estudiante> getCarrerasConEstudiantes() {
        return em.createQuery("SELECT c, COUNT(e)AS cantidad FROM Carrera c " +
                "JOIN EstudianteCarrera ec ON ec.carrera = c " +
                "JOIN ec.estudiante e " +
                "GROUP BY c " +
                "HAVING COUNT(e) > 0 " +
                "ORDER BY cantidad DESC", Estudiante.class).getResultList();
    }

    @Override
    public List<Estudiante> getEstudiantesByCarrera(Carrera c, String ciudad) {
        Carrera buscar = em.find(Carrera.class, c.getIdCarrera());
        if (buscar == null) {
            System.err.println("No se encontro la carrera");
            return new ArrayList<>();
        }

        return em.createQuery("SELECT e from EstudianteCarrera ec join ec.estudiante e join ec.carrera c where c.id_carrera= ?1 and e.ciudad LIKE ?2", Estudiante.class).setParameter(1, c.getIdCarrera()).setParameter(2, ciudad).getResultList();
    }

    @Override
    public void altaCarrera(int id, String nombre, int duracion) {
        Carrera nuevaCarrera = null;
        try {
            em.getTransaction().begin();
            if (id > 0 && nombre != null && duracion > 0) {
                nuevaCarrera = new Carrera(id, nombre, duracion);
                em.persist(nuevaCarrera);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                System.err.println("Faltan completar datos");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al crear carrera");
        }
    }
}
