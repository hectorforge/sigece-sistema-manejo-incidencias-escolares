package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AsignarResponsableRequestDto(
        @NotNull(message = "El ID del responsable es obligatorio")
        Long responsableId,

        @Size(max = 500, message = "El motivo no debe superar los 500 caracteres")
        String motivo
) {
}