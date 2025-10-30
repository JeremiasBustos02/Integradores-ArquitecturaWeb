package edu.empresa.factories;

import edu.empresa.impl.CarreraRepositoryImpl;
import edu.empresa.impl.EstudianteCarreraImpl;
import edu.empresa.impl.EstudianteRepositoryImpl;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteCarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DAOFactory {
    private static volatile DAOFactory instance = null;
    private final EntityManagerFactory emf;

    private DAOFactory() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CarreraRepository getCarreraRepository() {
        return CarreraRepositoryImpl.getInstance();
    }

    public EstudianteRepository getEstudianteRepository() {
        return EstudianteRepositoryImpl.getInstance();
    }

    public EstudianteCarreraRepository getEstudianteCarreraRepository() {
        return EstudianteCarreraImpl.getInstance();
    }
}