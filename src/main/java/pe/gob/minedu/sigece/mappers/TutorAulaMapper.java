package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.tutor.TutorAulaResponseDto;
import pe.gob.minedu.sigece.models.TutorAula;
import pe.gob.minedu.sigece.models.Usuario;

public final class TutorAulaMapper {
    private TutorAulaMapper() {}

    public static TutorAulaResponseDto toDto(TutorAula t) {
        Usuario u = t.getUsuario();
        return new TutorAulaResponseDto(
                t.getId(),
                u.getId(),
                u.getNombres() + " " + u.getApellidos(),
                u.getDni(),
                t.getNivel(),
                t.getGrado(),
                t.getSeccion(),
                t.getAnioEscolar(),
                t.isActivo(),
                t.getCreatedAt()
        );
    }
}
