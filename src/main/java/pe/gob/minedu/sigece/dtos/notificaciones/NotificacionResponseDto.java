package pe.gob.minedu.sigece.dtos.notificaciones;

import pe.gob.minedu.sigece.enums.CanalNotificacion;
import pe.gob.minedu.sigece.enums.EstadoEnvio;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificacionResponseDto(
        Long id,
        UUID incidenteId,
        Long apoderadoId,
        String apoderadoNombre,
        String enviadoPorNombre,
        Long alertaId,
        CanalNotificacion canal,
        String destino,
        String asunto,
        String mensaje,
        OffsetDateTime fechaEnvio,
        EstadoEnvio estadoEnvio,
        Integer intentosEnvio,
        String detalleResultado,
        String idTransaccionExterno,
        OffsetDateTime createdAt
) {
}