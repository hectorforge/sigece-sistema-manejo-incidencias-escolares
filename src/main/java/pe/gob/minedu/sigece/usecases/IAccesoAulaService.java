package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.models.TutorAula;
import pe.gob.minedu.sigece.models.Usuario;

import java.util.List;

public interface IAccesoAulaService {
    /** @return null = sin restricción (ADMINISTRADOR, PSICOLOGO); lista = solo esas aulas (TUTOR) */
    List<TutorAula> aulasRestringidas(Usuario usuario);
}
