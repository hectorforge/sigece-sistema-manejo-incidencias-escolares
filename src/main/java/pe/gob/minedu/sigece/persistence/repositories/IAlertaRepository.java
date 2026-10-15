package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.EstadoAlerta;
import pe.gob.minedu.sigece.enums.TipoAlerta;
import pe.gob.minedu.sigece.models.Alerta;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAlertaRepository extends JpaRepository<Alerta, Long>, JpaSpecificationExecutor<Alerta> {

    List<Alerta> findByIncidenteIdOrderByFechaGeneracionDesc(UUID incidenteId);

    @Query("""
            SELECT COUNT(a) FROM Alerta a
            WHERE (a.usuarioDestino.id = :usuarioId OR a.usuarioDestino IS NULL)
              AND a.estadoAlerta IN (pe.gob.minedu.sigece.enums.EstadoAlerta.PENDIENTE, pe.gob.minedu.sigece.enums.EstadoAlerta.VISTA)
            """)
    int countAlertasActivas(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Alerta a
            WHERE a.incidente.id = :incidenteId
              AND a.tipoAlerta = :tipoAlerta
              AND ((:estudianteId IS NULL AND a.estudiante IS NULL) OR a.estudiante.id = :estudianteId)
              AND a.estadoAlerta IN (pe.gob.minedu.sigece.enums.EstadoAlerta.PENDIENTE, pe.gob.minedu.sigece.enums.EstadoAlerta.VISTA)
            """)
    boolean existsAlertaActiva(@Param("incidenteId") UUID incidenteId,
                              @Param("tipoAlerta") TipoAlerta tipoAlerta,
                              @Param("estudianteId") Long estudianteId);
}