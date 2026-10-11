package pe.gob.minedu.sigece.dtos.estudiantes;

import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.models.Estudiante;

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
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static EstudianteResponseDto fromEntity(Estudiante estudiante) {
        return new EstudianteResponseDto(
                estudiante.getId(),
                estudiante.getCodigoSiagie(),
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getFechaNacimiento(),
                estudiante.getNivel(),
                estudiante.getGrado(),
                estudiante.getSeccion(),
                estudiante.getActivo(),
                estudiante.getCreatedAt(),
                estudiante.getUpdatedAt()
        );
    }
}