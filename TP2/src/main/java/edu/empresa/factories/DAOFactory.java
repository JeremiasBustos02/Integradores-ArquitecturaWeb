package edu.empresa.factories;

import edu.empresa.impl.CarreraRepositoryImpl;
import edu.empresa.impl.EstudianteRepositoryImpl;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

public class DAOFactory {
    private static DAOFactory instance = null;

    public DAOFactory() {
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public EstudianteRepository getEstudianteDAO(EntityManager em) {
        return new EstudianteRepositoryImpl(em);
    }

    public CarreraRepository getCarreraDAO(EntityManager em) {
        return new CarreraRepositoryImpl(em);
    }
}
