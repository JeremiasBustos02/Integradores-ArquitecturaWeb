package edu.empresa;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.EstudianteDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.entities.EstudianteCarrera;
import edu.empresa.factories.DAOFactory;
import edu.empresa.factories.JPAUtil;
import edu.empresa.repositories.CarreraRepository;
import edu.empresa.repositories.EstudianteRepository;
import edu.empresa.utils.DataDelete;
import edu.empresa.utils.DataLoader;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        new DataDelete().deleteAll();
        System.out.println("------------------------------------");

        new DataLoader().loadData();
        System.out.println("------------------------------------");

        Estudiante nuevoEstudiante = new Estudiante(
                42123678, "Nahuel", "Ruppel", 26, "Male", 249305, "Tandil"
        );

        System.out.println("Ejercicio a) - Dar de alta un estudiante");
        darAltaNuevoEstudiante(nuevoEstudiante);
        System.out.println("------------------------------------");

        int idCarrera = 1; // ID de la carrera TUDAI

        System.out.println("Ejercicio b) - Matricular un estudiante en una carrera");
        matricularEstudianteEnCarrera(nuevoEstudiante, idCarrera);
        System.out.println("------------------------------------");

        System.out.println("Ejercicio c) - Recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple");
        listarEstudiantesOrdenadosPorNombre();
        System.out.println("------------------------------------");

        System.out.println("Ejercicio d) - Recuperar un estudiante a partir de su LU");
        int luBuscado = 249305;
        recuperarEstudiantePorLU(luBuscado);
        System.out.println("------------------------------------");

        System.out.println("Ejercicio e) - Recuperar todos los estudiantes, en base a su género");
        String generoBuscado = "Female";
        recuperarEstudiantesPorGenero(generoBuscado);

        generoBuscado = "Male";
        recuperarEstudiantesPorGenero(generoBuscado);
        System.out.println("------------------------------------");

        System.out.println("Ejercicio F) - Recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos");
        recuperarCarrerasConInscriptos();
        System.out.println("------------------------------------");

        System.out.println("Ejercicio G) - Recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia");
        String ciudadResidencia = "Rauch";
        recuperarEstudiantesPorCarreraYCiudad(idCarrera, ciudadResidencia);
        System.out.println("------------------------------------");

    }


    private static void darAltaNuevoEstudiante(Estudiante nuevoEstudiante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoEstudiante);
            em.getTransaction().commit();

            System.out.println("Nuevo estudiante creado exitosamente:");
            System.out.println("   DNI: " + nuevoEstudiante.getDni());
            System.out.println("   Nombre: " + nuevoEstudiante.getNombre() + " " + nuevoEstudiante.getApellido());
            System.out.println("   Edad: " + nuevoEstudiante.getEdad());
            System.out.println("   Género: " + nuevoEstudiante.getGenero());
            System.out.println("   LU: " + nuevoEstudiante.getLu());
            System.out.println("   Ciudad: " + nuevoEstudiante.getCiudad());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            System.err.println("Error al crear el estudiante: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void matricularEstudianteEnCarrera(Estudiante estudiante, int idCarrera) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            DAOFactory factory = DAOFactory.getInstance();
            CarreraRepository carreraRepo = factory.getCarreraDAO(em);

            Carrera carreraTUDAI = carreraRepo.buscarCarreraPorId(idCarrera);

            if (carreraTUDAI != null) {
                EstudianteCarrera matricula = new EstudianteCarrera(estudiante, carreraTUDAI, 2025, 0, 0);
                em.persist(matricula);

                System.out.println("Matrícula exitosa:");
                System.out.println("   Estudiante: " + estudiante.getNombre() + " " + estudiante.getApellido());
                System.out.println("   Carrera: " + carreraTUDAI.getNombre());
                System.out.println("   Duración: " + carreraTUDAI.getDuracion() + " años");
                System.out.println("   Año de inscripción: 2025");
            } else {
                System.out.println("Error: No se encontró la carrera TUDAI");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            System.err.println("Error al matricular al estudiante: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void listarEstudiantesOrdenadosPorNombre() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DAOFactory factory = DAOFactory.getInstance();
            EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

            List<EstudianteDTO> estudiantes = estudianteRepo.buscarTodosOrderByNombre();

            System.out.println("LISTADO DE ESTUDIANTES ORDENADOS POR NOMBRE DESCENDENTEMENTE:");
            System.out.printf("%-10s %-15s %-15s %-5s %-8s %-10s %-15s%n",
                    "DNI", "NOMBRE", "APELLIDO", "EDAD", "GÉNERO", "LU", "CIUDAD");

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
            e.printStackTrace();
            System.err.println("Error al listar estudiantes: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void recuperarEstudiantePorLU(int lu) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DAOFactory factory = DAOFactory.getInstance();
            EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

            System.out.println("BÚSQUEDA DE ESTUDIANTE POR LU: " + lu);
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
            e.printStackTrace();
            System.err.println("Error al buscar estudiante por LU: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void recuperarEstudiantesPorGenero(String genero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DAOFactory factory = DAOFactory.getInstance();
            EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

            List<EstudianteDTO> estudiantes = estudianteRepo.buscarPorGenero(genero);

            System.out.println("LISTADO DE ESTUDIANTES " + genero + ":");

            if (estudiantes.isEmpty()) {
                System.out.println("No hay estudiantes " + genero.toLowerCase() + " registrados.");
            } else {
                System.out.printf("%-10s %-15s %-15s %-5s %-10s %-15s%n",
                        "DNI", "NOMBRE", "APELLIDO", "EDAD", "LU", "CIUDAD");

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
            e.printStackTrace();
            System.err.println("Error al buscar estudiantes por género: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public static void recuperarCarrerasConInscriptos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DAOFactory factory = DAOFactory.getInstance();
            CarreraRepository carreraRepo = factory.getCarreraDAO(em);

            List<CarreraDTO> carreras = carreraRepo.recuperarCarrerasConInscriptos();

            System.out.println("LISTADO DE CARRERAS CON ESTUDIANTES INSCRIPTOS (ORDENADO POR CANTIDAD DE INSCRIPTOS):");
            System.out.printf("%-10s %-25s %-10s %-15s%n",
                    "ID", "NOMBRE", "DURACIÓN", "CANT. INSCRIPTOS");

            for (CarreraDTO carrera : carreras) {
                System.out.printf("%-10d %-25s %-10d %-15d%n",
                        carrera.getIdCarrera(),
                        carrera.getNombre(),
                        carrera.getDuracion(),
                        carrera.getCantidadInscriptos()
                );
            }
            System.out.println("--------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al listar carreras con inscriptos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void recuperarEstudiantesPorCarreraYCiudad(int idCarrera, String ciudadResidencia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DAOFactory factory = DAOFactory.getInstance();
            CarreraRepository carreraRepo = factory.getCarreraDAO(em);
            EstudianteRepository estudianteRepo = factory.getEstudianteDAO(em);

            Carrera carrera = carreraRepo.buscarCarreraPorId(idCarrera);
            if (carrera == null) {
                System.out.println("No se encontró la carrera con ID: " + idCarrera);
                return;
            }

            List<EstudianteDTO> estudiantes = estudianteRepo.buscarTodosPorCarreraYCiudad(idCarrera, ciudadResidencia);

            System.out.println("LISTADO DE ESTUDIANTES INSCRIPTOS EN " + carrera.getNombre() +
                    " Y RESIDENTES EN " + ciudadResidencia + ":");

            if (estudiantes.isEmpty()) {
                System.out.println("No hay estudiantes inscriptos en " + carrera.getNombre() +
                        " que residan en " + ciudadResidencia + ".");
            } else {
                System.out.printf("%-10s %-15s %-15s %-5s %-12s %-10s %-15s%n",
                        "DNI", "NOMBRE", "APELLIDO", "EDAD", "GÉNERO", "LU", "CIUDAD");

                for (EstudianteDTO estudiante : estudiantes) {
                    System.out.printf("%-10d %-15s %-15s %-5d %-12s %-10d %-15s%n",
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al buscar estudiantes por carrera y ciudad: " + e.getMessage());
        } finally {
            em.close();
        }

    }
}
