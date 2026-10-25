package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.enums.*;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidenteResponseDto(
        UUID id,
        String codigoCaso,
        Long tipoIncidenciaId,
        String tipoIncidenciaNombre,
        Gravedad gravedad,
        String descripcion,
        String lugarOcurrencia,
        MedioOcurrencia medioOcurrencia,
        OffsetDateTime fechaOcurrencia,
        OffsetDateTime fechaRegistro,
        EstadoIncidente estado,
        Boolean notificarApoderado,
        Long reportadoPorId,
        String reportadoPorNombre,
        Long responsableActualId,
        String responsableActualNombre,
        OffsetDateTime fechaResolucion,
        String resueltoPorNombre,
        String actaCierre,
        String conclusionesFinales,
        OffsetDateTime fechaCierre,
        String cerradoPorNombre,
        Boolean reportadoSiseve,
        OffsetDateTime fechaReporteSiseve,
        String codigoSiseve,
        String observacionesSiseve,
        String derivadoPorNombre,
        OffsetDateTime createdAt
) {
}