package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionResponseDto;
import pe.gob.minedu.sigece.models.Notificacion;

public final class NotificacionMapper {
    private NotificacionMapper() {}

    public static NotificacionResponseDto toDto(Notificacion n) {
        return new NotificacionResponseDto(
                n.getId(),
                n.getIncidente().getId(),
                n.getApoderado().getId(),
                n.getApoderado().getNombres() + " " + n.getApoderado().getApellidos(),
                n.getEnviadoPor().getNombres() + " " + n.getEnviadoPor().getApellidos(),
                n.getAlerta() != null ? n.getAlerta().getId() : null,
                n.getCanal(),
                n.getDestino(),
                n.getAsunto(),
                n.getMensaje(),
                n.getFechaEnvio(),
                n.getEstadoEnvio(),
                n.getIntentosEnvio(),
                n.getDetalleResultado(),
                n.getIdTransaccionExterno(),
                n.getCreatedAt()
        );
    }
}