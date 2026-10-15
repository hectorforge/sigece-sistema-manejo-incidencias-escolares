package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CasoPendienteResponseDto(
        UUID incidenteId,
        String codigoCaso,
        Gravedad gravedad,
        EstadoIncidente estado,
        OffsetDateTime fechaRegistro,
        long horasSinAtencion,
        String responsableNombre
) {
}