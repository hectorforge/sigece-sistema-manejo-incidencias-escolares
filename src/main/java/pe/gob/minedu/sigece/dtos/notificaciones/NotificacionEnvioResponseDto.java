package pe.gob.minedu.sigece.dtos.notificaciones;

import pe.gob.minedu.sigece.enums.EstadoEnvio;

import java.time.OffsetDateTime;

public record NotificacionEnvioResponseDto(
        Long notificacionId,
        EstadoEnvio estadoEnvio,
        boolean entregadoExitosamente,
        String detalleProveedor,
        String idTransaccionExterno,
        OffsetDateTime fechaEnvio
) {
}