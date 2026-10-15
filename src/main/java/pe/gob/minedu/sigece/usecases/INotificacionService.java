package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.notificaciones.EstadoEnvioRequestDto;
import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionEnvioResponseDto;
import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionRequestDto;
import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de gestionar y auditar las notificaciones enviadas a los apoderados.
 * Casos de uso asociados: CU09, CU14, CU16.
 */
public interface INotificacionService {

    /**
     * Envía notificaciones a los apoderados del estudiante mediante los canales configurados (EMAIL, SMS, WhatsApp, etc.).
     * <p>Caso de uso: <b>CU16 · Notificar apoderados</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param incidenteId Identificador UUID del caso asociado.
     * @param dto         Canal, destinatarios, asunto y cuerpo del mensaje.
     * @param usuarioId   ID del usuario que autoriza y dispara la notificación.
     * @return Resultado del intento de envío.
     */
    NotificacionEnvioResponseDto notificarApoderados(UUID incidenteId, NotificacionRequestDto dto, Long usuarioId);

    /**
     * Actualiza el estado de recepción o entrega de una notificación (ej. ENTREGADO, REBOTADO, LEÍDO).
     * <p>Caso de uso: <b>CU16 · Notificar apoderados</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param notificacionId Identificador de la notificación enviada.
     * @param dto            Nuevo estado de entrega y constancia de recepción.
     * @return Notificación con el estado actualizado.
     */
    NotificacionResponseDto actualizarEstadoEnvio(Long notificacionId, EstadoEnvioRequestDto dto);

    /**
     * Reintenta el envío de una notificación cuyo intento previo haya fallado.
     * <p>Caso de uso: <b>CU16 · Notificar apoderados</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param notificacionId Identificador del registro fallido.
     * @param usuarioId      ID del usuario que autoriza el reintento.
     * @return Resultado del nuevo intento de envío.
     */
    NotificacionEnvioResponseDto reintentarEnvio(Long notificacionId, Long usuarioId);

    /**
     * Lista todas las comunicaciones y citaciones emitidas en el marco de un expediente específico.
     * <p>Caso de uso: <b>CU16 · Notificar apoderados, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del caso.
     * @return Historial de comunicaciones del caso.
     */
    List<NotificacionResponseDto> listarPorIncidente(UUID incidenteId);

    /**
     * Comprueba si los apoderados principales de las partes involucradas han sido efectivamente notificados.
     * <p>Caso de uso: <b>CU14 · Cerrar caso</b></p>
     * <p>Rol: Comité de Bienestar</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @return {@code true} si cuentan con constancia fehaciente de notificación; {@code false} en caso contrario.
     */
    boolean apoderadosPrincipalesNotificados(UUID incidenteId);

    /**
     * Lista todas las notificaciones históricas recibidas por un apoderado en particular.
     * <p>Caso de uso: <b>CU16 · Notificar apoderados</b></p>
     * <p>Rol: Personal de gestión, Administrador</p>
     *
     * @param apoderadoId Identificador único del apoderado.
     * @return Lista de notificaciones dirigidas al apoderado.
     */
    List<NotificacionResponseDto> listarPorApoderado(Long apoderadoId);
}