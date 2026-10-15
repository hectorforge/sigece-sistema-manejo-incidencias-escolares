package pe.gob.minedu.sigece.dtos.seguimiento;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AccionRevisionRequestDto(
        @NotNull(message = "La nueva fecha de revisión es obligatoria")
        @Future(message = "La fecha reprogramada debe ser posterior al día de hoy")
        LocalDate nuevaFechaRevision,

        @NotBlank(message = "La justificación de la postergación es obligatoria")
        String justificacion
) {
}