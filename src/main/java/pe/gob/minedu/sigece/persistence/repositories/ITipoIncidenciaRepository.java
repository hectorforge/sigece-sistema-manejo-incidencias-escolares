package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.models.TipoIncidencia;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Long>, JpaSpecificationExecutor<TipoIncidencia> {

    Optional<TipoIncidencia> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<TipoIncidencia> findAllByActivoTrue();

    List<TipoIncidencia> findAllByCategoriaSiseveAndActivoTrue(CategoriaSiseve categoriaSiseve);

    List<TipoIncidencia> findAllByCategoriaSiseveAndGravedadBaseAndActivoTrue(CategoriaSiseve categoriaSiseve, Gravedad gravedadBase);
}