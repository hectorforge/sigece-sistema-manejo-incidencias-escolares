package pe.gob.minedu.sigece.enums;

/**
 * Ciclo de vida (workflow) del caso de convivencia escolar,
 * modelado conforme al proceso BPMN de atención de incidencias.
 */
public enum EstadoIncidente {
    REGISTRADO,
    EN_INVESTIGACION,
    EN_SEGUIMIENTO,
    RESUELTO,
    DERIVADO_SISEVE,
    CERRADO
}
