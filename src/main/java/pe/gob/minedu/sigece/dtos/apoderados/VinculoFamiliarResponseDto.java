package pe.gob.minedu.sigece.dtos.apoderados;

import pe.gob.minedu.sigece.enums.Parentesco;

public record VinculoFamiliarResponseDto(
        Long estudianteId,
        Long apoderadoId,
        String nombreApoderado,
        String nombreEstudiante,
        String numeroDocumento,
        Parentesco parentesco,
        Boolean esContactoPrincipal,
        String telefono,
        String correo
) {
}