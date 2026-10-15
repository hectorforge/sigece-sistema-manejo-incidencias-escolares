package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.models.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEstudianteRepository extends JpaRepository<Estudiante, Long>, JpaSpecificationExecutor<Estudiante> {

    Optional<Estudiante> findByCodigoSiagie(String codigoSiagie);

    boolean existsByCodigoSiagie(String codigoSiagie);

    List<Estudiante> findAllByGradoAndSeccionAndNivelAndActivoTrue(String grado, String seccion, Nivel nivel);

    @Query("SELECT COUNT(DISTINCT ii.incidente.id) FROM IncidenteInvolucrado ii WHERE ii.estudiante.id = :estudianteId")
    int countIncidentesByEstudianteId(@Param("estudianteId") Long estudianteId);
}