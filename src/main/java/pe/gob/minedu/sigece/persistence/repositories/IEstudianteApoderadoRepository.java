package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.EstudianteApoderado;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEstudianteApoderadoRepository extends JpaRepository<EstudianteApoderado, Long> {

    List<EstudianteApoderado> findByEstudianteId(Long estudianteId);

    List<EstudianteApoderado> findByApoderadoId(Long apoderadoId);

    Optional<EstudianteApoderado> findByEstudianteIdAndApoderadoId(Long estudianteId, Long apoderadoId);

    Optional<EstudianteApoderado> findByEstudianteIdAndEsContactoPrincipalTrue(Long estudianteId);

    void deleteByEstudianteIdAndApoderadoId(Long estudianteId, Long apoderadoId);

    @Modifying
    @Query("UPDATE EstudianteApoderado ea SET ea.esContactoPrincipal = false WHERE ea.estudiante.id = :estudianteId")
    void desmarcarContactosPrincipales(@Param("estudianteId") Long estudianteId);

    @Modifying
    @Query("UPDATE EstudianteApoderado ea SET ea.esContactoPrincipal = true WHERE ea.estudiante.id = :estudianteId AND ea.apoderado.id = :apoderadoId")
    int marcarComoContactoPrincipal(@Param("estudianteId") Long estudianteId, @Param("apoderadoId") Long apoderadoId);
}