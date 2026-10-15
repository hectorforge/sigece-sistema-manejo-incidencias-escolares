package pe.gob.minedu.sigece.dtos.auditoria;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.AccionAuditoria;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;
import pe.gob.minedu.sigece.enums.ResultadoAuditoria;

import java.util.UUID;

public record AuditoriaRequestDto(
        Long usuarioId,
        String usernameIntentado,

        @NotNull(message = "La acción es obligatoria")
        AccionAuditoria accion,

        @NotNull(message = "La entidad es obligatoria")
        EntidadAuditoria entidad,

        @Size(max = 64)
        String entidadId,

        UUID incidenteId,
        String valorAnterior,
        String valorNuevo,

        @Size(max = 500)
        String descripcion,

        @Size(max = 45)
        String ipOrigen,

        @Size(max = 255)
        String userAgent,

        @NotNull(message = "El resultado es obligatorio")
        ResultadoAuditoria resultado
) {
}