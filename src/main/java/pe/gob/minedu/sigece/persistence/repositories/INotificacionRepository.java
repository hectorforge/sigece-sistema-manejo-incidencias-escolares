package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.Notificacion;

import java.util.List;
import java.util.UUID;

@Repository
public interface INotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByIncidenteIdOrderByCreatedAtDesc(UUID incidenteId);

    List<Notificacion> findByApoderadoIdOrderByCreatedAtDesc(Long apoderadoId);

    @Query("""
            SELECT COUNT(n) FROM Notificacion n
            INNER JOIN EstudianteApoderado ea ON ea.apoderado.id = n.apoderado.id
            WHERE n.incidente.id = :incidenteId
              AND ea.esContactoPrincipal = true
              AND n.estadoEnvio IN (pe.gob.minedu.sigece.enums.EstadoEnvio.ENVIADO, pe.gob.minedu.sigece.enums.EstadoEnvio.ENTREGADO, pe.gob.minedu.sigece.enums.EstadoEnvio.LEIDO)
            """)
    long countNotificacionesEfectivasApoderadosPrincipales(@Param("incidenteId") UUID incidenteId);
}