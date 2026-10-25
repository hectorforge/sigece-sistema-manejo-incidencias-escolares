package pe.gob.minedu.sigece.dtos.notificaciones;

import pe.gob.minedu.sigece.enums.CanalNotificacion;
import pe.gob.minedu.sigece.enums.EstadoEnvio;
import pe.gob.minedu.sigece.enums.OrigenNotificacion;
import pe.gob.minedu.sigece.enums.TipoNotificacion;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificacionResponseDto(
        Long id,
        UUID incidenteId,
        Long estudianteId,
        Long apoderadoId,
        String apoderadoNombre,
        Long accionId,
        Long plantillaId,
        String enviadoPorNombre,
        TipoNotificacion tipoNotificacion,
        OrigenNotificacion origen,
        CanalNotificacion canal,
        String destino,
        String asunto,
        String mensaje,
        OffsetDateTime fechaEnvio,
        OffsetDateTime fechaEntrega,
        EstadoEnvio estadoEnvio,
        Integer intentosEnvio,
        String detalleResultado,
        String idTransaccionExterno,
        OffsetDateTime createdAt
) {
}