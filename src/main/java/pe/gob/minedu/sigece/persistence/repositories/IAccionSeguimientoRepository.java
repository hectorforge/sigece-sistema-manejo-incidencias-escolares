package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.models.AccionSeguimiento;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IAccionSeguimientoRepository extends JpaRepository<AccionSeguimiento, Long>, JpaSpecificationExecutor<AccionSeguimiento> {

    List<AccionSeguimiento> findByIncidenteIdOrderByFechaAccionDesc(UUID incidenteId);

    List<AccionSeguimiento> findByUsuarioIdAndEstadoAccionNot(Long usuarioId, EstadoAccion estadoAccion);

    @Query("""
            SELECT a FROM AccionSeguimiento a
            WHERE a.estadoAccion <> pe.gob.minedu.sigece.enums.EstadoAccion.COMPLETADA
              AND a.fechaProximaRevision IS NOT NULL
              AND a.fechaProximaRevision < :fechaActual
            """)
    List<AccionSeguimiento> findAccionesVencidas(@Param("fechaActual") LocalDate fechaActual);

    @Query("SELECT COUNT(a) FROM AccionSeguimiento a WHERE a.incidente.id = :incidenteId AND a.estadoAccion <> pe.gob.minedu.sigece.enums.EstadoAccion.COMPLETADA")
    long countAccionesPendientesPorIncidente(@Param("incidenteId") UUID incidenteId);
}