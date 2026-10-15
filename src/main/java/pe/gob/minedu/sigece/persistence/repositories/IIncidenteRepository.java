package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.models.Incidente;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IIncidenteRepository extends JpaRepository<Incidente, UUID>, JpaSpecificationExecutor<Incidente> {

    Optional<Incidente> findByCodigoCaso(String codigoCaso);

    boolean existsByCodigoCaso(String codigoCaso);

    @Query("SELECT i FROM Incidente i WHERE i.estado = pe.gob.minedu.sigece.enums.EstadoIncidente.REGISTRADO AND i.fechaRegistro <= :fechaLimite")
    List<Incidente> findCasosDemorados(@Param("fechaLimite") OffsetDateTime fechaLimite);

    @Query("""
            SELECT i FROM Incidente i
            INNER JOIN IncidenteInvolucrado ii ON ii.incidente.id = i.id
            WHERE ii.estudiante.id = :estudianteId
            ORDER BY i.fechaOcurrencia DESC
            """)
    List<Incidente> findAllByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Query("""
            SELECT COUNT(DISTINCT i) FROM Incidente i
            INNER JOIN IncidenteInvolucrado ii ON ii.incidente.id = i.id
            WHERE ii.estudiante.id = :estudianteId
              AND ii.rolInvolucrado IN (pe.gob.minedu.sigece.enums.RolInvolucrado.AGRESOR, pe.gob.minedu.sigece.enums.RolInvolucrado.VICTIMA)
              AND i.fechaOcurrencia >= :fechaDesde
            """)
    int countReincidenciasPorEstudiante(@Param("estudianteId") Long estudianteId, @Param("fechaDesde") OffsetDateTime fechaDesde);

    @Query("""
            SELECT i FROM Incidente i
            WHERE i.gravedad IN (pe.gob.minedu.sigece.enums.Gravedad.GRAVE, pe.gob.minedu.sigece.enums.Gravedad.MUY_GRAVE)
              AND i.reportadoSiseve = false
              AND i.estado <> pe.gob.minedu.sigece.enums.EstadoIncidente.CERRADO
            """)
    List<Incidente> findPendientesDeDerivacionSiseve();

    long countByEstado(EstadoIncidente estado);
    long countByReincidenciaTrue();
    long countByReportadoSiseveTrue();
}