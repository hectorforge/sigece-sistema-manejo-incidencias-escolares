package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.Apoderado;

import java.util.List;
import java.util.Optional;

@Repository
public interface IApoderadoRepository extends JpaRepository<Apoderado, Long>, JpaSpecificationExecutor<Apoderado> {

    Optional<Apoderado> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    @Query("SELECT ea.apoderado FROM EstudianteApoderado ea WHERE ea.estudiante.id = :estudianteId")
    List<Apoderado> findAllByEstudianteId(@Param("estudianteId") Long estudianteId);
}