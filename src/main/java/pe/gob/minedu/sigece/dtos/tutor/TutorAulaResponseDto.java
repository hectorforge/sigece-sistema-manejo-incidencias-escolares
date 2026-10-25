package pe.gob.minedu.sigece.dtos.tutor;

import pe.gob.minedu.sigece.enums.Nivel;

import java.time.OffsetDateTime;

public record TutorAulaResponseDto(
        Long id,
        Long usuarioId,
        String tutorNombre,
        String tutorDni,
        Nivel nivel,
        String grado,
        String seccion,
        int anioEscolar,
        boolean activo,
        OffsetDateTime createdAt
) {
}
