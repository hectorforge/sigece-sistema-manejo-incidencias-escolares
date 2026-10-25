package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionResponseDto;
import pe.gob.minedu.sigece.models.Apoderado;
import pe.gob.minedu.sigece.models.Notificacion;
import pe.gob.minedu.sigece.models.Usuario;

public final class NotificacionMapper {
    private NotificacionMapper() {}

    private static String nombre(String n, String a) { return n + " " + a; }

    public static NotificacionResponseDto toDto(Notificacion n) {
        Apoderado ap = n.getApoderado();
        Usuario env = n.getEnviadoPor();
        return new NotificacionResponseDto(
                n.getId(), n.getIncidente().getId(), n.getEstudiante().getId(),
                ap != null ? ap.getId() : null,
                ap != null ? nombre(ap.getNombres(), ap.getApellidos()) : null,
                n.getAccion() != null ? n.getAccion().getId() : null,
                n.getPlantilla().getId(),
                env != null ? nombre(env.getNombres(), env.getApellidos()) : "Automático",
                n.getTipoNotificacion(), n.getOrigen(), n.getCanal(), n.getDestino(),
                n.getAsunto(), n.getMensaje(), n.getFechaEnvio(), n.getFechaEntrega(),
                n.getEstadoEnvio(), n.getIntentosEnvio(), n.getDetalleResultado(),
                n.getIdTransaccionExterno(), n.getCreatedAt());
    }
}