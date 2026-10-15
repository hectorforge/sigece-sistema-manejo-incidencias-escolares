package pe.gob.minedu.sigece.enums;

/**
 * Mapea: auditoria_eventos.accion
 * CONSTRAINT: chk_auditoria_accion
 */
public enum AccionAuditoria {
    LOGIN_EXITOSO,
    LOGIN_FALLIDO,
    LOGOUT,
    CAMBIO_PASSWORD,
    CREAR,
    MODIFICAR,
    DESACTIVAR,
    CONSULTAR,
    CAMBIO_ESTADO,
    CAMBIO_GRAVEDAD,
    ASIGNAR_RESPONSABLE,
    CIERRE_CASO,
    DERIVACION_SISEVE,
    SUBIR_EVIDENCIA,
    ELIMINAR_EVIDENCIA,
    ENVIAR_NOTIFICACION,
    GENERAR_ALERTA,
    RESOLVER_ALERTA,
    EXPORTAR_REPORTE
}