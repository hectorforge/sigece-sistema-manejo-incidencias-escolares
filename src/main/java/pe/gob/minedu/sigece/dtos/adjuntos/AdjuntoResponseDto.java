package pe.gob.minedu.sigece.dtos.adjuntos;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdjuntoResponseDto(
        Long id,
        UUID incidenteId,
        Long accionId,
        Long subidoPorId,
        String subidoPorNombre,
        String nombreOriginal,
        String descripcion,
        String tipoMime,
        Long tamanoBytes,
        String urlStorage,
        String hashSha256,
        Boolean activo,
        OffsetDateTime createdAt
) {
}