package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.enums.MedioOcurrencia;

import java.time.OffsetDateTime;

public record IncidenteRequestDto(
        @NotNull(message = "El tipo de incidencia es obligatorio")
        Long tipoIncidenciaId,
        @NotNull(message = "La gravedad inicial es obligatoria")
        Gravedad gravedad,
        @NotBlank(message = "La descripción de los hechos es obligatoria")
        String descripcion,
        @NotBlank(message = "El lugar es obligatorio") @Size(max = 150)
        String lugarOcurrencia,
        MedioOcurrencia medioOcurrencia,          // null => PRESENCIAL
        @NotNull(message = "La fecha de ocurrencia es obligatoria")
        OffsetDateTime fechaOcurrencia,
        Boolean notificarApoderado,               // null => false
        Long responsableId                        // null => quien reporta
) {}