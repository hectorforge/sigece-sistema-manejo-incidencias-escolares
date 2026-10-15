package pe.gob.minedu.sigece.dtos.siseve;

import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SisevePendienteResponseDto(
        UUID incidenteId,
        String codigoCaso,
        String tipoIncidenciaNombre,
        Gravedad gravedad,
        OffsetDateTime fechaOcurrencia,
        long diasTranscurridos
) {
}