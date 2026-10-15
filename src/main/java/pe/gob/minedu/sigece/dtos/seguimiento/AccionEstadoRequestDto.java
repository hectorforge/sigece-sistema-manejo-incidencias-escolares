package pe.gob.minedu.sigece.dtos.seguimiento;

import jakarta.validation.constraints.NotNull;
import pe.gob.minedu.sigece.enums.EstadoAccion;

public record AccionEstadoRequestDto(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoAccion nuevoEstado,

        String comentarioEjecucion
) {
}