package pe.gob.minedu.sigece.dtos.estudiantes;

import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record EstudianteResponseDto(
        Long id,
        String codigoSiagie,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        Nivel nivel,
        String grado,
        String seccion,
        Boolean activo,
        OffsetDateTime createdAt
) {
}