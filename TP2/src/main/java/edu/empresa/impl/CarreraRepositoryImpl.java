package edu.empresa.impl;

import edu.empresa.repositories.CarreraRepository;
import jakarta.persistence.EntityManager;

public class CarreraRepositoryImpl implements CarreraRepository {
    EntityManager em;

    public CarreraRepositoryImpl(EntityManager em) {
        this.em = em;
    }
}
