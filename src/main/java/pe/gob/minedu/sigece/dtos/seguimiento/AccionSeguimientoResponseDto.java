package pe.gob.minedu.sigece.dtos.seguimiento;

import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.enums.TipoAccion;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccionSeguimientoResponseDto(
        Long id,
        UUID incidenteId,
        Long usuarioId,
        String usuarioNombre,
        TipoAccion tipoAccion,
        String descripcion,
        OffsetDateTime fechaAccion,
        LocalDate fechaProximaRevision,
        EstadoAccion estadoAccion,
        OffsetDateTime fechaCompletada,
        OffsetDateTime createdAt
) {
}