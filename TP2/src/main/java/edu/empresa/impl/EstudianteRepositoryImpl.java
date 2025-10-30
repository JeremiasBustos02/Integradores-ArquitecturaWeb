package edu.empresa.impl;

import edu.empresa.dto.EstudianteDTO;
import edu.empresa.entities.Estudiante;
import edu.empresa.factories.DAOFactory;
import edu.empresa.repositories.EstudianteRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class EstudianteRepositoryImpl implements EstudianteRepository {
    private final EntityManager em;
    private static volatile EstudianteRepositoryImpl instance;

    private EstudianteRepositoryImpl() {
        this.em = DAOFactory.getInstance().getEntityManager();
    }

    public static EstudianteRepositoryImpl getInstance() {
        if (instance == null) {
            synchronized (EstudianteRepositoryImpl.class) {
                if (instance == null) {
                    instance = new EstudianteRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Estudiante altaEstudiante(int dni, String nombre, String apellido, int edad, String genero, int lu, String ciudad) {
        Estudiante nuevoEstudiante = null;
        try {
            em.getTransaction().begin();
            if (dni > 0 && nombre != null && apellido != null && edad > 0 && genero != null && lu > 0 && ciudad != null) {
                nuevoEstudiante = new Estudiante(dni, nombre, apellido, edad, genero, lu, ciudad);
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
    public Estudiante buscarPorDNI(int dni) {
        return em.find(Estudiante.class, dni);
    }


    // Métodos que retornan DTOs
    @Override
    public EstudianteDTO buscarPorLU(int lu) {
        try {
            Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.lu = :lu", Estudiante.class)
                    .setParameter("lu", lu)
                    .getSingleResult();
            return convertirADTO(estudiante);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<EstudianteDTO> buscarTodos() {
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e", Estudiante.class)
                .getResultList();
        return convertirListaADTO(estudiantes);
    }

    @Override
    public List<EstudianteDTO> buscarPorGenero(String genero) {
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.genero = ?1", Estudiante.class)
                .setParameter(1, genero)
                .getResultList();
        return convertirListaADTO(estudiantes);
    }

    @Override
    public List<EstudianteDTO> buscarTodosOrderByNombre() {
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.nombre DESC", Estudiante.class)
                .getResultList();
        return convertirListaADTO(estudiantes);
    }

    // Métodos auxiliares para conversión
    private EstudianteDTO convertirADTO(Estudiante estudiante) {
        if (estudiante == null) return null;
        return new EstudianteDTO(
                estudiante.getDni(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getLu(),
                estudiante.getCiudad()
        );
    }

    private List<EstudianteDTO> convertirListaADTO(List<Estudiante> estudiantes) {
        List<EstudianteDTO> dtos = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            dtos.add(convertirADTO(estudiante));
        }
        return dtos;
    }

    @Override
    public List<EstudianteDTO> buscarTodosPorCarreraYCiudad(int idCarrera, String ciudad) {
        List<Estudiante> estudiantes = em.createQuery(
                        "SELECT e from EstudianteCarrera ec " +
                                "join ec.estudiante e " +
                                "join ec.carrera c " +
                                "where c.id_carrera= ?1 " +
                                "   and e.ciudad LIKE ?2", Estudiante.class)
                .setParameter(1, idCarrera)
                .setParameter(2, ciudad)
                .getResultList();
        return convertirListaADTO(estudiantes);
    }
}
