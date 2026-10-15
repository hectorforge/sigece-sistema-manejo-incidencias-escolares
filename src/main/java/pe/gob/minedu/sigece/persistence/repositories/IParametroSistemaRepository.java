package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.ParametroSistema;

@Repository
public interface IParametroSistemaRepository extends JpaRepository<ParametroSistema, String> {
}