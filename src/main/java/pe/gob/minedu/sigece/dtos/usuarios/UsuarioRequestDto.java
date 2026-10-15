package pe.gob.minedu.sigece.dtos.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres")
        String apellidos,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo tiene un formato inválido")
        @Size(max = 150, message = "El correo no debe superar los 150 caracteres")
        String correo,

        @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
        String telefono,

        @NotNull(message = "El rol es obligatorio")
        Long roleId,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}