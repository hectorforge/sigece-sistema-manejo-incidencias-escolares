package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.models.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCodigoSiagie(String codigoSiagie);

    boolean existsByCodigoSiagie(String codigoSiagie);

    List<Estudiante> findAllByGradoAndSeccionAndNivelAndActivoTrue(String grado, String seccion, Nivel nivel);

    @Query(value = "SELECT COUNT(*) FROM incidentes WHERE estudiante_id = :estudianteId", nativeQuery = true)
    int countIncidentesByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Modifying
    @Query(value = "INSERT INTO estudiante_apoderado (estudiante_id, apoderado_id, parentesco, es_contacto_principal) " +
            "VALUES (:estudianteId, :apoderadoId, :parentesco, :esContactoPrincipal) " +
            "ON CONFLICT (estudiante_id, apoderado_id) DO UPDATE " +
            "SET parentesco = EXCLUDED.parentesco, es_contacto_principal = EXCLUDED.es_contacto_principal",
            nativeQuery = true)
    void upsertRelacionApoderado(@Param("estudianteId") Long estudianteId,
                                 @Param("apoderadoId") Long apoderadoId,
                                 @Param("parentesco") String parentesco,
                                 @Param("esContactoPrincipal") boolean esContactoPrincipal);
}