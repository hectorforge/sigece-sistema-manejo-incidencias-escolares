package pe.gob.minedu.sigece.dtos.incidentes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record DerivacionSiseveRequestDto(
        @NotBlank
        @Size(max = 50)
        String codigoSiseve,

        @NotNull
        @PastOrPresent
        OffsetDateTime fechaReporteSiseve,

        String observacionesSiseve
) {
}
