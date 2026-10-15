package pe.gob.minedu.sigece.dtos.apoderados;

import pe.gob.minedu.sigece.enums.TipoDocumento;

import java.time.OffsetDateTime;

public record ApoderadoResponseDto(
        Long id,
        String nombres,
        String apellidos,
        TipoDocumento tipoDocumento,
        String numeroDocumento,
        String telefono,
        String correo,
        OffsetDateTime createdAt
) {
}