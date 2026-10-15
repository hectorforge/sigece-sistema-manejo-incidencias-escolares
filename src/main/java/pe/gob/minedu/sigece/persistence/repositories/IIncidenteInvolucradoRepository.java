package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.RolInvolucrado;
import pe.gob.minedu.sigece.models.IncidenteInvolucrado;

import java.util.List;
import java.util.UUID;

@Repository
public interface IIncidenteInvolucradoRepository extends JpaRepository<IncidenteInvolucrado, Long> {

    List<IncidenteInvolucrado> findByIncidenteId(UUID incidenteId);

    List<IncidenteInvolucrado> findByEstudianteIdAndRolInvolucrado(Long estudianteId, RolInvolucrado rolInvolucrado);

    boolean existsByIncidenteIdAndEstudianteIdAndRolInvolucrado(UUID incidenteId, Long estudianteId, RolInvolucrado rol);

    boolean existsByIncidenteIdAndUsuarioIdAndRolInvolucrado(UUID incidenteId, Long usuarioId, RolInvolucrado rol);
}