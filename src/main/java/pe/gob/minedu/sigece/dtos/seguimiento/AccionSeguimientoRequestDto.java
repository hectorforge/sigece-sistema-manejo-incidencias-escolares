package pe.gob.minedu.sigece.dtos.seguimiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.gob.minedu.sigece.enums.TipoAccion;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AccionSeguimientoRequestDto(
        @NotNull(message = "El tipo de acción es obligatorio")
        TipoAccion tipoAccion,

        @NotBlank(message = "La descripción de la medida o acuerdo es obligatoria")
        String descripcion,

        OffsetDateTime fechaAccion,

        LocalDate fechaProximaRevision
) {
}