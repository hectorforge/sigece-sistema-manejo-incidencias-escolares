package pe.gob.minedu.sigece.dtos.apoderados;

import jakarta.validation.constraints.NotNull;
import pe.gob.minedu.sigece.enums.Parentesco;

public record AsignarApoderadoRequestDto(
        @NotNull(message = "El ID del apoderado es obligatorio")
        Long apoderadoId,

        @NotNull(message = "El parentesco es obligatorio")
        Parentesco parentesco,

        @NotNull(message = "Debe indicar si es el contacto principal")
        Boolean esContactoPrincipal
) {
}