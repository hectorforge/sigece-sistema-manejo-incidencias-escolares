package pe.gob.minedu.sigece.dtos.apoderados;

import pe.gob.minedu.sigece.enums.TipoDocumento;
import pe.gob.minedu.sigece.models.Apoderado;

import java.time.OffsetDateTime;

public record ApoderadoResponseDto(
        Long id,
        String nombres,
        String apellidos,
        TipoDocumento tipoDocumento,
        String numeroDocumento,
        String telefono,
        String correo,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ApoderadoResponseDto fromEntity(Apoderado apoderado) {
        return new ApoderadoResponseDto(
                apoderado.getId(),
                apoderado.getNombres(),
                apoderado.getApellidos(),
                apoderado.getTipoDocumento(),
                apoderado.getNumeroDocumento(),
                apoderado.getTelefono(),
                apoderado.getCorreo(),
                apoderado.getCreatedAt(),
                apoderado.getUpdatedAt()
        );
    }
}