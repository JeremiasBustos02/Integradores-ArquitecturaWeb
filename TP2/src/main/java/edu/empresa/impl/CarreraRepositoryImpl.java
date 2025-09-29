package edu.empresa.impl;

import edu.empresa.dto.CarreraDTO;
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
        return em.createQuery("SELECT c, COUNT(e) AS cantidad " +
                "FROM Carrera c " +
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

        return em.createQuery(
                "SELECT e from EstudianteCarrera ec " +
                "join ec.estudiante e " +
                "join ec.carrera c " +
                "where c.id_carrera= ?1 " +
                "   and e.ciudad LIKE ?2", Estudiante.class)
                .setParameter(1, c.getIdCarrera())
                .setParameter(2, ciudad)
                .getResultList();
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
            e.printStackTrace();
            System.err.println("Error al crear carrera");
        }
    }

    @Override
    public Carrera buscarCarreraPorId(int id) {
        return em.find(Carrera.class, id);
    }

    @Override
    public CarreraDTO buscarPorId(int id) {
        Carrera carrera = em.find(Carrera.class, id);
        if (carrera != null) {
            return new CarreraDTO(
                carrera.getIdCarrera(),
                carrera.getNombre(),
                carrera.getDuracion()
            );
        }
        return null;
    }

    @Override
    public List<CarreraDTO> recuperarCarrerasConInscriptos() {
        String jpql = "SELECT new edu.empresa.dto.CarreraDTO(c.id_carrera, c.nombre, c.duracion, COUNT(ec)) " +
                "FROM Carrera c " +
                "JOIN c.estudiantes ec " +
                "GROUP BY c.id_carrera, c.nombre, c.duracion " +
                "HAVING COUNT(ec) > 0 " +
                "ORDER BY COUNT(ec) DESC";

        List<CarreraDTO> resultados = em.createQuery(jpql, CarreraDTO.class).getResultList();
        return resultados;
    }

}
