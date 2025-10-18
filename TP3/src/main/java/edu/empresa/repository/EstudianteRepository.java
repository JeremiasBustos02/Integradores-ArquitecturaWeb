package edu.empresa.repository;

import edu.empresa.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    @Query("""
            SELECT E from Estudiante E order by E.apellido 
            """)
    List<Estudiante> getEstudianteByAlphabetOrder();

    /**
     * Busca un estudiante por su número de libreta universitaria (LU)
     *
     * @param lu número de libreta universitaria
     * @return Optional con el estudiante si existe
     */
    Optional<Estudiante> findByLu(int lu);

    /**
     * Busca estudiantes por género
     *
     * @param genero género a buscar
     * @return lista de estudiantes con ese género
     */
    List<Estudiante> findByGenero(String genero);

}