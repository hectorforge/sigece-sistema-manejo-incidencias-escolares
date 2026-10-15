package pe.gob.minedu.sigece.dtos.alertas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.EstadoAlerta;

public record AtenderAlertaRequestDto(
        @NotNull(message = "El nuevo estado de la alerta es obligatorio")
        EstadoAlerta nuevoEstado, // ATENDIDA o DESCARTADA

        @NotBlank(message = "La observación o descargo de la resolución es obligatoria")
        @Size(max = 255, message = "La observación no debe superar los 255 caracteres")
        String observacionResolucion
) {
}