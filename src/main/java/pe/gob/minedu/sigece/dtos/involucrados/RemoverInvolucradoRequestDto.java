package pe.gob.minedu.sigece.dtos.involucrados;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RemoverInvolucradoRequestDto(
        @NotBlank(message = "Debe justificar el motivo del retiro del involucrado")
        @Size(max = 255, message = "La justificación no debe superar los 255 caracteres")
        String motivo
) {
}