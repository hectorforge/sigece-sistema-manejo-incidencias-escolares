package pe.gob.minedu.sigece.dtos.adjuntos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdjuntoRequestDto(
        Long accionId,

        @NotBlank(message = "La descripción de la evidencia es obligatoria")
        @Size(min = 3, max = 500, message = "La descripción debe tener entre 3 y 500 caracteres")
        String descripcion
) {
}