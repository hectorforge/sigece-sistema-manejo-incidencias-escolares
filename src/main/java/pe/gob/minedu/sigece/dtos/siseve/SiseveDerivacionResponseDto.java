package pe.gob.minedu.sigece.dtos.siseve;

import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.ModoDerivacion;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SiseveDerivacionResponseDto(
        UUID id,
        String codigoCaso,
        Boolean reportadoSiseve,
        String codigoSiseve,
        OffsetDateTime fechaReporteSiseve,
        String observacionesSiseve,
        String derivadoPor
) {
}