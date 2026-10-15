package pe.gob.minedu.sigece.dtos.adjuntos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EliminarAdjuntoRequestDto(
        @NotBlank(message = "Debe indicar el motivo de la eliminación")
        @Size(max = 255, message = "El motivo no debe superar los 255 caracteres")
        String motivoEliminacion
) {
}