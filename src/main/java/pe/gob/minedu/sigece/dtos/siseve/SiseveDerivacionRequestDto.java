package pe.gob.minedu.sigece.dtos.siseve;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.ModoDerivacion;

public record SiseveDerivacionRequestDto(
        @NotBlank(message = "El código asignado por SíSeVe es obligatorio")
        @Size(max = 50, message = "El código SíSeVe no debe superar los 50 caracteres")
        String codigoSiseve,

        @NotNull(message = "El modo de derivación es obligatorio")
        ModoDerivacion modoDerivacion,

        String observacionesSiseve
) {
}