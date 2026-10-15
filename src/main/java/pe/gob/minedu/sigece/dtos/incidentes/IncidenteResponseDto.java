package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.enums.ModoDerivacion;

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
        OffsetDateTime fechaOcurrencia,
        OffsetDateTime fechaRegistro,
        EstadoIncidente estado,
        Long reportadoPorId,
        String reportadoPorNombre,
        Long responsableActualId,
        String responsableActualNombre,
        Boolean reincidencia,
        String actaCierre,
        OffsetDateTime fechaCierre,
        String cerradoPorNombre,
        Boolean reportadoSiseve,
        EstadoDerivacion estadoDerivacion,
        ModoDerivacion modoDerivacion,
        String codigoSiseve,
        OffsetDateTime createdAt
) {
}