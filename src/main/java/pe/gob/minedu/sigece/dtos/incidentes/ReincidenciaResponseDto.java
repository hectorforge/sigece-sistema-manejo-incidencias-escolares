package pe.gob.minedu.sigece.dtos.incidentes;

import java.util.List;
import java.util.UUID;

public record ReincidenciaResponseDto(
        Long estudianteId,
        boolean superaUmbral,
        int totalIncidentesEnPeriodo,
        int umbralConfigurado,
        int periodoDiasEvaluado,
        List<UUID> incidentesIds
) {
}