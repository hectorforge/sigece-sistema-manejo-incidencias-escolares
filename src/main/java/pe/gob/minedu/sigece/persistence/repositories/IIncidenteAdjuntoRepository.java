package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.IncidenteAdjunto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IIncidenteAdjuntoRepository extends JpaRepository<IncidenteAdjunto, Long> {

    List<IncidenteAdjunto> findByIncidenteIdAndActivoTrueOrderByCreatedAtDesc(UUID incidenteId);

    Optional<IncidenteAdjunto> findByNombreAlmacenamiento(String nombreAlmacenamiento);

    boolean existsByIncidenteIdAndHashSha256AndActivoTrue(UUID incidenteId, String hashSha256);
}