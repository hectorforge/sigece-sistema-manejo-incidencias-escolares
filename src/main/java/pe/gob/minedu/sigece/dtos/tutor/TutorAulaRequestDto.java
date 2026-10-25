package pe.gob.minedu.sigece.dtos.tutor;

import jakarta.validation.constraints.*;
import pe.gob.minedu.sigece.enums.Nivel;

public record TutorAulaRequestDto(
        @NotNull(message = "El ID del tutor es obligatorio")
        Long usuarioId,

        @NotNull(message = "El nivel es obligatorio")
        Nivel nivel,

        @NotBlank(message = "El grado es obligatorio")
        @Size(max = 20, message = "El grado no debe superar los 20 caracteres")
        String grado,

        @NotBlank(message = "La sección es obligatoria")
        @Size(max = 10, message = "La sección no debe superar los 10 caracteres")
        String seccion,

        @Min(value = 2000, message = "El año escolar debe ser mayor o igual a 2000")
        @Max(value = 2100, message = "El año escolar debe ser menor o igual a 2100")
        Integer anioEscolar
) {
}
