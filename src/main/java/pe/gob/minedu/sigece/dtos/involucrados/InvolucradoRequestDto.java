package pe.gob.minedu.sigece.dtos.involucrados;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.RolInvolucrado;

public record InvolucradoRequestDto(
        Long estudianteId,
        Long usuarioId,

        @NotNull(message = "El rol del involucrado es obligatorio")
        RolInvolucrado rolInvolucrado,

        @Size(max = 255, message = "Las observaciones no deben superar los 255 caracteres")
        String observaciones
) {
}