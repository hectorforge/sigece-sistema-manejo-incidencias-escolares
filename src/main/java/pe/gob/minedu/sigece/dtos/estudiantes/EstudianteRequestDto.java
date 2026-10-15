package pe.gob.minedu.sigece.dtos.estudiantes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;

public record EstudianteRequestDto(
        @NotBlank(message = "El código SIAGIE es obligatorio")
        @Size(max = 20, message = "El código SIAGIE no debe superar los 20 caracteres")
        String codigoSiagie,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres")
        String apellidos,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
        LocalDate fechaNacimiento,

        @NotNull(message = "El nivel educativo es obligatorio")
        Nivel nivel,

        @NotBlank(message = "El grado es obligatorio")
        @Size(max = 20, message = "El grado no debe superar los 20 caracteres")
        String grado,

        @NotBlank(message = "La sección es obligatoria")
        @Size(max = 10, message = "La sección no debe superar los 10 caracteres")
        String seccion
) {
}