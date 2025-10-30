package edu.empresa.impl;

import edu.empresa.dto.CarreraDTO;
import edu.empresa.dto.GenerarReporteDTO;
import edu.empresa.entities.Carrera;
import edu.empresa.entities.Estudiante;
import edu.empresa.factories.DAOFactory;
import edu.empresa.factories.JPAUtil;
import edu.empresa.repositories.CarreraRepository;
import jakarta.persistence.EntityManager;

import java.util.*;

public class CarreraRepositoryImpl implements CarreraRepository {
    private final  EntityManager em;
    public static volatile CarreraRepositoryImpl instance;

    private CarreraRepositoryImpl() {
        this.em = DAOFactory.getInstance().getEntityManager();;
    }
    public static CarreraRepositoryImpl getInstance(){
        if(instance == null){
            synchronized (CarreraRepositoryImpl.class){
                if (instance == null){
                    instance=new CarreraRepositoryImpl();
                }
            }
        }
        return instance;
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
    public List<CarreraDTO> obtenerTodasCarreras() {
        List<Carrera> carreras = em.createQuery("SELECT C FROM Carrera C", Carrera.class).getResultList();
        if (carreras == null) return new ArrayList<>();
        List<CarreraDTO> cDtos = new ArrayList<>();
        for (Carrera c : carreras) {
            CarreraDTO carrera = new CarreraDTO(c.getIdCarrera(), c.getNombre(), c.getDuracion());
            cDtos.add(carrera);
        }
        return cDtos;
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

    @Override
    public List<GenerarReporteDTO> generarReporteCarreras() {
        // obtenemos años de inscripcion y graduacion sin repetir
        List<Integer> anios = em.createQuery(
                "SELECT DISTINCT ec.inscripcion FROM EstudianteCarrera ec WHERE ec.inscripcion IS NOT NULL AND ec.inscripcion > 0 " +
                        "UNION " +
                        "SELECT DISTINCT ec.graduacion FROM EstudianteCarrera ec WHERE ec.graduacion IS NOT NULL AND ec.graduacion > 0",
                Integer.class
        ).getResultList();

        List<GenerarReporteDTO> reporte = new ArrayList<>();

        // contamos graduados y inscriptos por año
        for (Integer anio : anios) {
            List<Object[]> datosAnio = em.createQuery(
                            "SELECT c.nombre, c.id_carrera, " +
                                    "COUNT(CASE WHEN ec.inscripcion = :anio THEN 1 ELSE NULL END), " +
                                    "COUNT(CASE WHEN ec.graduacion = :anio THEN 1 ELSE NULL END) " +
                                    "FROM EstudianteCarrera ec JOIN ec.carrera c " +
                                    "WHERE ec.inscripcion = :anio OR ec.graduacion = :anio " +
                                    "GROUP BY c.nombre, c.id_carrera " +
                                    "ORDER BY c.nombre",
                            Object[].class
                    ).setParameter("anio", anio)
                    .getResultList();
            //los asignamos al dto
            for (Object[] dato : datosAnio) {
                String nombreCarrera = (String) dato[0];
                int idCarrera = ((Number) dato[1]).intValue();
                long inscriptos = ((Number) dato[2]).longValue();
                long egresados = ((Number) dato[3]).longValue();

                if (inscriptos > 0 || egresados > 0) {
                    reporte.add(new GenerarReporteDTO(nombreCarrera, idCarrera, anio, inscriptos, egresados));
                }
            }
        }


        reporte.sort(Comparator
                .comparing(GenerarReporteDTO::getNombreCarrera)
                .thenComparing(GenerarReporteDTO::getAnio));

        return reporte;
    }
}
