package edu.empresa;


import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.EstudianteDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.factories.DAOFactory;
import edu.empresa.factories.JPAUtil;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteCarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import edu.empresa.utils.DataDelete;
import edu.empresa.utils.DataLoader;
import jakarta.persistence.EntityManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        new DataDelete().deleteAll();
        System.out.println("------------------------------------");

        new DataLoader().loadData();
        System.out.println("------------------------------------");

        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Dar de alta un nuevo estudiante
            Estudiante estudianteCreado = darAltaNuevoEstudiante(em);
            System.out.println("------------------------------------");

            // Matricular al estudiante en una carrera
            if (estudianteCreado != null) {
                matricularEstudianteEnCarrera(em, estudianteCreado);
                System.out.println("------------------------------------");
            }
            
            listarEstudiantesOrdenadosPorNombre(em);
            System.out.println("------------------------------------");
            
            // Recuperar un estudiante por LU
            recuperarEstudiantePorLU(em, 249305);
            System.out.println("------------------------------------");
            
            // Recuperar estudiantes por género
            recuperarEstudiantesPorGenero(em, "Female"); // Femeninos
            System.out.println("------------------------------------");
            
            recuperarEstudiantesPorGenero(em, "Male"); // Masculinos
            System.out.println("------------------------------------");
            
            em.getTransaction().commit();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error en la operación completa: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static Estudiante darAltaNuevoEstudiante(EntityManager em) {
        try {
            Estudiante nuevoEstudiante = new Estudiante(
                    42123678,           // DNI único
                    "Nahuel",           // Nombre
                    "Ruppel",           // Apellido
                    26,                 // Edad
                    "Male",                // Género
                    249305,             // LU único
                    "Tandil"            // Ciudad
            );
            
            em.persist(nuevoEstudiante);

            System.out.println("Nuevo estudiante creado exitosamente:");
            System.out.println("   DNI: " + nuevoEstudiante.getDni());
            System.out.println("   Nombre: " + nuevoEstudiante.getNombre() + " " + nuevoEstudiante.getApellido());
            System.out.println("   Edad: " + nuevoEstudiante.getEdad());
            System.out.println("   Género: " + nuevoEstudiante.getGenero());
            System.out.println("   LU: " + nuevoEstudiante.getLu());
            System.out.println("   Ciudad: " + nuevoEstudiante.getCiudad());
            
            return nuevoEstudiante;
            
        } catch (Exception e) {
            System.err.println("Error al crear el estudiante: " + e.getMessage());
            throw e;
        }
    }

    private static void matricularEstudianteEnCarrera(EntityManager em, Estudiante estudiante) {
        DAOFactory factory = DAOFactory.getInstance();
        CarreraRepository carreraRepo = factory.getCarreraDAO(em);

        try {
            Carrera carreraTUDAI = carreraRepo.buscarCarreraPorId(1);
            
            if (carreraTUDAI != null) {
                EstudianteCarrera matricula = new EstudianteCarrera(
                    estudiante,         // Estudiante
                    carreraTUDAI,       // Carrera TUDAI
                    2025,               // Año de inscripción
                    0,                  // Año de graduación (0 = no graduado aún)
                    0                   // Antigüedad inicial
                );
                
                em.persist(matricula);
                
                System.out.println("Matrícula exitosa:");
                System.out.println("   Estudiante: " + estudiante.getNombre() + " " + estudiante.getApellido());
                System.out.println("   Carrera: " + carreraTUDAI.getNombre());
                System.out.println("   Duración: " + carreraTUDAI.getDuracion() + " años");
                System.out.println("   Año de inscripción: 2025");
            } else {
                System.out.println("Error: No se encontró la carrera TUDAI");
            }
        } catch (Exception e) {
            System.err.println("Error al matricular al estudiante: " + e.getMessage());
            throw e;
        }
    }
    
    private static void listarEstudiantesOrdenadosPorNombre(EntityManager em) {
        DAOFactory factory = DAOFactory.getInstance();
        EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

        try {
            List<EstudianteDTO> estudiantes = estudianteRepo.buscarTodosOrderByNombre();
            
            System.out.println("LISTADO DE ESTUDIANTES ORDENADOS POR NOMBRE:");
            System.out.println("================================================");
            System.out.printf("%-10s %-15s %-15s %-5s %-8s %-10s %-15s%n", 
                "DNI", "NOMBRE", "APELLIDO", "EDAD", "GÉNERO", "LU", "CIUDAD");
            System.out.println("--------------------------------------------------------------------------------");
            
            for (EstudianteDTO estudiante : estudiantes) {
                System.out.printf("%-10d %-15s %-15s %-5d %-8s %-10d %-15s%n",
                    estudiante.getDni(),
                    estudiante.getNombre(),
                    estudiante.getApellido(),
                    estudiante.getEdad(),
                    estudiante.getGenero(),
                    estudiante.getLu(),
                    estudiante.getCiudad()
                );
            }
            
            System.out.println("--------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
            throw e;
        }
    }

    private static void recuperarEstudiantePorLU(EntityManager em, int lu) {
        DAOFactory factory = DAOFactory.getInstance();
        EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

        try {
            System.out.println("BÚSQUEDA DE ESTUDIANTE POR LU: " + lu);
            System.out.println("===============================================");
            
            EstudianteDTO estudiante = estudianteRepo.buscarPorLU(lu);
            
            if (estudiante != null) {
                System.out.println("Estudiante encontrado:");
                System.out.println("   DNI: " + estudiante.getDni());
                System.out.println("   Nombre completo: " + estudiante.getNombre() + " " + estudiante.getApellido());
                System.out.println("   Edad: " + estudiante.getEdad() + " años");
                System.out.println("   Género: " + estudiante.getGenero());
                System.out.println("   LU: " + estudiante.getLu());
                System.out.println("   Ciudad: " + estudiante.getCiudad());
            } else {
                System.out.println("No se encontró ningún estudiante con LU: " + lu);
            }
            
        } catch (Exception e) {
            System.err.println("Error al buscar estudiante por LU: " + e.getMessage());
            throw e;
        }
    }

    private static void recuperarEstudiantesPorGenero(EntityManager em, String genero) {
        DAOFactory factory = DAOFactory.getInstance();
        EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

        try {
            String tipoGenero = genero;
            System.out.println("LISTADO DE ESTUDIANTES " + tipoGenero + ":");
            System.out.println("================================================");
            
            List<EstudianteDTO> estudiantes = estudianteRepo.buscarPorGenero(genero);
            
            if (estudiantes.isEmpty()) {
                System.out.println("No hay estudiantes " + tipoGenero.toLowerCase() + " registrados.");
            } else {
                System.out.printf("%-10s %-15s %-15s %-5s %-10s %-15s%n", 
                    "DNI", "NOMBRE", "APELLIDO", "EDAD", "LU", "CIUDAD");
                System.out.println("------------------------------------------------------------------------");
                
                for (EstudianteDTO estudiante : estudiantes) {
                    System.out.printf("%-10d %-15s %-15s %-5d %-10d %-15s%n",
                        estudiante.getDni(),
                        estudiante.getNombre(),
                        estudiante.getApellido(),
                        estudiante.getEdad(),
                        estudiante.getLu(),
                        estudiante.getCiudad()
                    );
                }
                
                System.out.println("------------------------------------------------------------------------");
            }
            
        } catch (Exception e) {
            System.err.println("Error al buscar estudiantes por género: " + e.getMessage());
            throw e;
        }
    }
}