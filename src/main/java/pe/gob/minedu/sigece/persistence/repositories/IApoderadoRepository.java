package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.Apoderado;

import java.util.List;
import java.util.Optional;

@Repository
public interface IApoderadoRepository extends JpaRepository<Apoderado, Long> {

    Optional<Apoderado> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    @Query(value = """
            SELECT a.* FROM apoderados a
            INNER JOIN estudiante_apoderado ea ON a.id = ea.apoderado_id
            WHERE ea.estudiante_id = :estudianteId
            """, nativeQuery = true)
    List<Apoderado> findAllByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Modifying
    @Query(value = "UPDATE estudiante_apoderado SET es_contacto_principal = false WHERE estudiante_id = :estudianteId", nativeQuery = true)
    void desmarcarContactosPrincipales(@Param("estudianteId") Long estudianteId);

    @Modifying
    @Query(value = "UPDATE estudiante_apoderado SET es_contacto_principal = true WHERE estudiante_id = :estudianteId AND apoderado_id = :apoderadoId", nativeQuery = true)
    int marcarComoContactoPrincipal(@Param("estudianteId") Long estudianteId, @Param("apoderadoId") Long apoderadoId);
}