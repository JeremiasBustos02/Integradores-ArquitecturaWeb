package edu.empresa.factories;

import edu.empresa.impl.CarreraRepositoryImpl;
import edu.empresa.impl.EstudianteCarreraImpl;
import edu.empresa.impl.EstudianteRepositoryImpl;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteCarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

public class DAOFactory {
    private static volatile DAOFactory instance = null;
    private final EntityManager em;

    private DAOFactory() {
        this.em = JPAUtil.getEntityManager();
    }

    // Instancias singleton de los repositorios
    private CarreraRepositoryImpl carreraRepository;
    private EstudianteRepositoryImpl estudianteRepository;
    private EstudianteCarreraImpl estudianteCarreraRepository;

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
//dao
    public CarreraRepository getCarreraRepository() {
        if (carreraRepository == null) {
            carreraRepository = CarreraRepositoryImpl.getInstance(em);
        }
        return carreraRepository;
    }
//dao
    public EstudianteRepository getEstudianteRepository() {
        if (estudianteRepository == null) {
            estudianteRepository = EstudianteRepositoryImpl.getInstance(em);
        }
        return estudianteRepository;
    }
//dao
    public EstudianteCarreraRepository getEstudianteCarreraRepository() {
        if (estudianteCarreraRepository == null) {
            estudianteCarreraRepository = EstudianteCarreraImpl.getInstance(em);
        }
        return estudianteCarreraRepository;
    }



    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
