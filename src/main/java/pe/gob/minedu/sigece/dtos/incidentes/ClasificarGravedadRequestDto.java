package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.gob.minedu.sigece.enums.Gravedad;

public record ClasificarGravedadRequestDto(
        @NotNull(message = "El nivel de gravedad es obligatorio")
        Gravedad gravedad,

        @NotBlank(message = "La justificación técnica es obligatoria")
        String justificacion
) {
}