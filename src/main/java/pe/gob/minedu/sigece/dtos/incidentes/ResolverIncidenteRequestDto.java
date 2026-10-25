package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.Size;

public record ResolverIncidenteRequestDto(
        @Size(max = 500)
        String observaciones
) {
}
