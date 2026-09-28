package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.CanalNotificacion;
import pe.edu.cibertec.sigece.domain.enums.EstadoEnvio;
import pe.edu.cibertec.sigece.service.dto.NotificacionDTO;
import pe.edu.cibertec.sigece.service.dto.NotificacionEnvioResultDTO;

import java.util.List;
import java.util.UUID;

/**
 * Erradica la falla estructural de la agenda escolar en papel: envía
 * y audita las comunicaciones a los apoderados (EMAIL, SMS, WHATSAPP,
 * llamada o citación presencial) generando constancia legal exigible
 * ante una eventual fiscalización de UGEL/SíSeVe.
 */
public interface NotificacionService {

    /**
     * Genera y envía una notificación a él/los apoderado(s) del(los)
     * estudiante(s) involucrado(s) en el incidente, priorizando el
     * contacto marcado como es_contacto_principal.
     */
    NotificacionEnvioResultDTO notificarApoderados(UUID incidenteId, CanalNotificacion canal,
                                                    String asunto, String mensaje, Long usuarioId);

    /**
     * Actualiza el estado de entrega de una notificación a partir del
     * webhook/callback del proveedor externo (SendGrid, Twilio, WhatsApp
     * Cloud API), registrando el id_transaccion_externo como evidencia.
     */
    NotificacionDTO actualizarEstadoEnvio(Long notificacionId, EstadoEnvio nuevoEstado, String idTransaccionExterno);

    /**
     * Reintenta el envío de una notificación marcada como FALLIDO.
     */
    NotificacionEnvioResultDTO reintentarEnvio(Long notificacionId, Long usuarioId);

    /**
     * Lista todas las notificaciones enviadas sobre un incidente, con
     * su estado de entrega, para trazabilidad ante el apoderado y auditoría.
     */
    List<NotificacionDTO> listarPorIncidente(UUID incidenteId);

    /**
     * Verifica si el (los) apoderado(s) principal(es) de un incidente ya
     * fueron notificados exitosamente (ENTREGADO/LEIDO), condición
     * habitualmente exigida antes de avanzar de estado el expediente.
     */
    boolean apoderadosPrincipalesNotificados(UUID incidenteId);

    /**
     * Historial de comunicaciones dirigidas a un apoderado específico,
     * independientemente del incidente (vista consolidada del padre/madre).
     */
    List<NotificacionDTO> listarPorApoderado(Long apoderadoId);
}