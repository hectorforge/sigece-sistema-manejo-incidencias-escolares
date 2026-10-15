package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;
import pe.gob.minedu.sigece.models.AuditoriaEvento;

import java.util.List;

@Repository
public interface IAuditoriaEventoRepository extends JpaRepository<AuditoriaEvento, Long>, JpaSpecificationExecutor<AuditoriaEvento> {

    List<AuditoriaEvento> findByEntidadAndEntidadIdOrderByFechaEventoDesc(EntidadAuditoria entidad, String entidadId);
}