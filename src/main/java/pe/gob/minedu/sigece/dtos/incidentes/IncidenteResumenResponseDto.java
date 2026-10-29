package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record IncidenteResumenResponseDto(
        UUID id,
        String codigoCaso,
        String tipoIncidenciaNombre,
        Gravedad gravedad,
        String lugarOcurrencia,
        OffsetDateTime fechaOcurrencia,
        EstadoIncidente estado,
        String responsableNombre,
        List<String>estudiantes
) {
}