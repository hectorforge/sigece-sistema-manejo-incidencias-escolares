package pe.gob.minedu.sigece.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambioPasswordRequestDto(
        @NotBlank(message = "La contraseña actual es obligatoria")
        String passwordActual,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, max = 50, message = "La nueva contraseña debe tener entre 8 y 50 caracteres")
        String nuevoPassword,

        @NotBlank(message = "Debe confirmar la nueva contraseña")
        String confirmacionPassword
) {
}