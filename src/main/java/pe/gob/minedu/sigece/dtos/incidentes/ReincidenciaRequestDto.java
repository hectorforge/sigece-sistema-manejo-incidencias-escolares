package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ReincidenciaRequestDto(
        @NotNull(message = "El ID del estudiante es obligatorio")
        Long estudianteId,

        OffsetDateTime fechaReferencia
) {
}