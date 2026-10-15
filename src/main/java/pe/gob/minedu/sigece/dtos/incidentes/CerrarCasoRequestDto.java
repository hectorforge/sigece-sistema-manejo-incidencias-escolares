package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CerrarCasoRequestDto(
        @NotBlank(message = "El acta de cierre es obligatoria")
        @Size(min = 10, message = "El acta de cierre debe tener al menos 10 caracteres")
        String actaCierre,

        String conclusionesFinales
) {
}