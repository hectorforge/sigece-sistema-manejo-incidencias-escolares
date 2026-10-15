package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.OffsetDateTime;

public record IncidenteRequestDto(
        @NotNull(message = "El tipo de incidencia es obligatorio")
        Long tipoIncidenciaId,

        @NotNull(message = "La gravedad inicial es obligatoria")
        Gravedad gravedad,

        @NotBlank(message = "La descripción de los hechos es obligatoria")
        String descripcion,

        @NotBlank(message = "El lugar de ocurrencia es obligatorio")
        @Size(max = 150, message = "El lugar no debe superar los 150 caracteres")
        String lugarOcurrencia,

        @NotNull(message = "La fecha y hora de ocurrencia es obligatoria")
        OffsetDateTime fechaOcurrencia
) {
}