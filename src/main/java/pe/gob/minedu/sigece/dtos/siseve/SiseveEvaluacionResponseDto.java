package pe.gob.minedu.sigece.dtos.siseve;

import java.util.UUID;

public record SiseveEvaluacionResponseDto(
        UUID incidenteId,
        String codigoCaso,
        boolean derivacionObligatoria,
        String baseLegal,
        String recomendacionTecnica
) {
}