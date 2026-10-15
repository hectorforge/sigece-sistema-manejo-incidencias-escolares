package pe.gob.minedu.sigece.dtos.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRegistroRequestDto(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres")
        String apellidos,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^[0-9]{8,15}$", message = "El DNI debe tener entre 8 y 15 dígitos numéricos")
        String dni,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo tiene un formato inválido")
        @Size(max = 150, message = "El correo no debe superar los 150 caracteres")
        String correo,

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 50, message = "El nombre de usuario no debe superar los 50 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
        String telefono,

        @NotNull(message = "El rol es obligatorio")
        Long roleId
) {
}