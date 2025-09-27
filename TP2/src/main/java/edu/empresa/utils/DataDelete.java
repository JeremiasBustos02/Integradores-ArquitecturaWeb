package edu.empresa.utils;

import edu.empresa.factories.JPAUtil;
import jakarta.persistence.EntityManager;

public class DataDelete {
    public void deleteAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM EstudianteCarrera").executeUpdate();
            em.createQuery("DELETE FROM Estudiante").executeUpdate();
            em.createQuery("DELETE FROM Carrera").executeUpdate();

            em.getTransaction().commit();
            System.out.println("Todos los datos eliminados correctamente.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}
