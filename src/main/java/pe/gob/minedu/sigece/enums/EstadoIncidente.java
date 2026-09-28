package pe.gob.minedu.sigece.enums;

/**
 * Estados del expediente, alineados a las fases del D.S. N.° 004-2018-MINEDU:
 * Registro -> Investigación -> Seguimiento -> Resolución / Derivación -> Cierre.
 */
public enum EstadoIncidente {
    REGISTRADO,
    EN_INVESTIGACION,
    EN_SEGUIMIENTO,
    RESUELTO,
    DERIVADO_SISEVE,
    CERRADO
}