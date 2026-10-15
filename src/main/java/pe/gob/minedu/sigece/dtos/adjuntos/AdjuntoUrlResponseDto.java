package pe.gob.minedu.sigece.dtos.adjuntos;

import java.time.OffsetDateTime;

public record AdjuntoUrlResponseDto(
        Long adjuntoId,
        String nombreOriginal,
        String urlDescargaTemporal,
        OffsetDateTime expiraEn
) {
}