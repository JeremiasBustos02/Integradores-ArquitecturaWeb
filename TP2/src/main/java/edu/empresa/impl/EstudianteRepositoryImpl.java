package edu.empresa.impl;

import edu.empresa.entities.Estudiante;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class EstudianteRepositoryImpl implements EstudianteRepository {
    EntityManager em;

    public EstudianteRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Estudiante altaEstudiante(int dni,String nombre, String apellido, int edad, String genero, int lu, String ciudad) {
        Estudiante nuevoEstudiante = null;
        try {
            em.getTransaction().begin();
            if (dni > 0 && nombre != null && apellido != null && edad > 0 && genero != null && lu > 0 && ciudad != null) {
                nuevoEstudiante = new Estudiante(dni,nombre, apellido, edad, genero, lu, ciudad);
                em.persist(nuevoEstudiante);
                em.getTransaction().commit();
                return nuevoEstudiante;
            } else {
                em.getTransaction().rollback();
                System.err.println("Faltan completar datos");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al crear estudiante");
        }
        return nuevoEstudiante;
    }

    @Override
    public Estudiante buscarPorLU(int lu) {
        return em.find(Estudiante.class, lu);
    }

    @Override
    public List<Estudiante> buscarTodos() {
        return em.createQuery("SELECT e " +
                "FROM Estudiante e ", Estudiante.class)
                .getResultList();
    }

    @Override
    public List<Estudiante> buscarPorGenero(String genero) {

        return em.createQuery("SELECT e " +
                "FROM Estudiante e " +
                "WHERE e.genero LIKE ?1", Estudiante.class)
                .setParameter(1, genero)
                .getResultList();
    }

    @Override
    public List<Estudiante> buscarTodosOrderByNombre() {
        return em.createQuery("SELECT E " +
                "FROM Estudiante E " +
                "order by nombre desc", Estudiante.class)
                .getResultList();
    }

}
