package pe.gob.minedu.sigece.dtos.siseve;

import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.ModoDerivacion;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SiseveDerivacionResponseDto(
        UUID incidenteId,
        String codigoCaso,
        Boolean reportadoSiseve,
        EstadoDerivacion estadoDerivacion,
        ModoDerivacion modoDerivacion,
        String codigoSiseve,
        OffsetDateTime fechaReporteSiseve,
        String observacionesSiseve,
        String derivadoPorNombre
) {
}