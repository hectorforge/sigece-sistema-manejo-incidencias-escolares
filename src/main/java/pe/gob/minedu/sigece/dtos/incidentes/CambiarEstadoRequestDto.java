package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.gob.minedu.sigece.enums.EstadoIncidente;

public record CambiarEstadoRequestDto(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoIncidente nuevoEstado,

        @NotBlank(message = "El motivo del cambio de estado es obligatorio")
        String motivo
) {
}