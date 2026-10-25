package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.models.TutorAula;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITutorAulaRepository extends JpaRepository<TutorAula, Long> {

    List<TutorAula> findByUsuarioIdAndAnioEscolarAndActivoTrue(Long usuarioId, int anioEscolar);

    List<TutorAula> findByUsuarioIdOrderByAnioEscolarDescNivelAscGradoAscSeccionAsc(Long usuarioId);

    List<TutorAula> findByNivelAndGradoAndSeccionAndAnioEscolarAndActivoTrue(
            String nivel, String grado, String seccion, int anioEscolar);

    Optional<TutorAula> findByUsuarioIdAndNivelAndGradoAndSeccionAndAnioEscolar(
            Long usuarioId, String nivel, String grado, String seccion, int anioEscolar);
    List<TutorAula> findByNivelAndGradoAndSeccionAndAnioEscolarAndActivoTrue(Nivel nivel, String grado, String seccion, int anioEscolar);

    Optional<TutorAula> findByUsuarioIdAndNivelAndGradoAndSeccionAndAnioEscolar(Long usuarioId, Nivel nivel, String grado, String seccion, int anioEscolar);
}
