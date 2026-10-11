package pe.gob.minedu.sigece.dtos.apoderados;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.TipoDocumento;

public record ApoderadoRequestDto(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres")
        String apellidos,

        @NotNull(message = "El tipo de documento es obligatorio")
        TipoDocumento tipoDocumento,

        @NotBlank(message = "El número de documento es obligatorio")
        @Size(min = 8, max = 15, message = "El número de documento debe tener entre 8 y 15 caracteres")
        String numeroDocumento,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
        String telefono,

        @Email(message = "El formato del correo es inválido")
        @Size(max = 150, message = "El correo no debe superar los 150 caracteres")
        String correo
) {
}