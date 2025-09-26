package edu.empresa.impl;

import edu.empresa.entities.Estudiante;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EstudianteRepositoryImpl implements EstudianteRepository {
    EntityManager em;

    public EstudianteRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Estudiante buscarPorLU(int lu) {
        return em.find(Estudiante.class, lu);
    }

    @Override
    public List<Estudiante> buscarTodos() {
        return em.createQuery("SELECT e FROM Estudiante e", Estudiante.class).getResultList();
    }
}
