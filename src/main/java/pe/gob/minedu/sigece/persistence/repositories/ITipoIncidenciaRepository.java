package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.models.TipoIncidencia;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Long> {

    Optional<TipoIncidencia> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<TipoIncidencia> findAllByActivoTrue();

    List<TipoIncidencia> findAllByCategoriaSiseveAndActivoTrue(CategoriaSiseve categoriaSiseve);
}