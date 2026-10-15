package pe.gob.minedu.sigece.dtos.auditoria;

import pe.gob.minedu.sigece.enums.AccionAuditoria;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;
import pe.gob.minedu.sigece.enums.ResultadoAuditoria;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AuditoriaResponseDto(
        Long id,
        OffsetDateTime fechaEvento,
        Long usuarioId,
        String usuarioNombre,
        String usernameIntentado,
        AccionAuditoria accion,
        EntidadAuditoria entidad,
        String entidadId,
        UUID incidenteId,
        String valorAnterior,
        String valorNuevo,
        String descripcion,
        String ipOrigen,
        String userAgent,
        ResultadoAuditoria resultado
) {
}