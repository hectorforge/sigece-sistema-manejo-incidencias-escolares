package pe.gob.minedu.sigece.utils;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

    // Errores de Validación y Petición
    VALIDATION_ERROR("ERR_VAL_001", "Error de validación en los campos enviados", "Revise los campos requeridos y formatos en la sección 'details'."),
    BAD_REQUEST("ERR_REQ_001", "Solicitud inválida o mal formada", "Verifique que los parámetros y el cuerpo de la solicitud cumplan con el formato esperado."),

    // Errores de Estudiantes
    ESTUDIANTE_NOT_FOUND("ERR_EST_001", "Estudiante no encontrado", "Verifique que el ID o código SIAGIE exista en la base de datos."),
    ESTUDIANTE_SIAGIE_DUPLICADO("ERR_EST_002", "El código SIAGIE ya se encuentra registrado", "Ingrese un código SIAGIE único y válido."),

    // Errores de Apoderados
    APODERADO_NOT_FOUND("ERR_APO_001", "Apoderado no encontrado", "Verifique que el ID o número de documento del apoderado exista en el sistema."),
    APODERADO_DOCUMENTO_DUPLICADO("ERR_APO_002", "El documento del apoderado ya está registrado", "Verifique el número de documento de identidad ingresado."),
    RELACION_APODERADO_NOT_FOUND("ERR_APO_003", "No se encontró el vínculo entre el estudiante y el apoderado", "Asegúrese de haber asignado primero al apoderado antes de marcarlo como principal."),

    // Errores Generales del Servidor
    INTERNAL_SERVER_ERROR("ERR_SRV_001", "Ocurrió un error interno en el servidor", "Intente nuevamente más tarde o comuníquese con la mesa de ayuda de MINEDU.");

    private final String code;
    private final String message;
    private final String suggestion;

    ErrorCatalog(String code, String message, String suggestion) {
        this.code = code;
        this.message = message;
        this.suggestion = suggestion;
    }
}