package pe.gob.minedu.sigece.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCatalog {

    /**
     * Errores generales de peticion y/o validacion
     */
    VALIDATION_ERROR(
            "ERR_VAL_001",
            "Error de validación en los campos enviados",
            "Revise los campos indicados en details.",
            HttpStatus.BAD_REQUEST
    ),

    BAD_REQUEST(
            "ERR_REQ_001",
            "Solicitud inválida o mal formada",
            "Verifique los parámetros y el cuerpo de la solicitud.",
            HttpStatus.BAD_REQUEST
    ),

    MISSING_PARAMETER(
            "ERR_REQ_002",
            "Falta un parámetro obligatorio",
            "Verifique que haya enviado todos los parámetros requeridos.",
            HttpStatus.BAD_REQUEST
    ),

    RESOURCE_NOT_FOUND(
            "ERR_RES_001",
            "El recurso solicitado no existe",
            "Verifique el identificador y la ruta de la solicitud.",
            HttpStatus.NOT_FOUND
    ),

    METHOD_NOT_ALLOWED(
            "ERR_HTTP_001",
            "El método HTTP no está permitido para este recurso",
            "Verifique si el endpoint admite GET, POST, PUT, PATCH o DELETE.",
            HttpStatus.METHOD_NOT_ALLOWED
    ),

    UNSUPPORTED_MEDIA_TYPE(
            "ERR_HTTP_002",
            "El tipo de contenido enviado no es compatible",
            "Verifique el encabezado Content-Type y los formatos admitidos.",
            HttpStatus.UNSUPPORTED_MEDIA_TYPE
    ),

    PAYLOAD_TOO_LARGE(
            "ERR_HTTP_003",
            "El contenido de la solicitud supera el tamaño permitido",
            "Reduzca el tamaño del contenido enviado.",
            HttpStatus.PAYLOAD_TOO_LARGE
    ),

    /**
     * Autenticacion y Autorizacion
     */
    UNAUTHENTICATED(
            "ERR_AUTH_001",
            "El usuario no está autenticado",
            "Inicie sesión para acceder al recurso solicitado.",
            HttpStatus.UNAUTHORIZED
    ),

    INVALID_CREDENTIALS(
            "ERR_AUTH_002",
            "Las credenciales proporcionadas no son válidas",
            "Verifique su usuario y contraseña.",
            HttpStatus.UNAUTHORIZED
    ),

    ACCESS_DENIED(
            "ERR_AUTH_003",
            "No tiene permisos para realizar esta operación",
            "Solicite los permisos correspondientes al administrador.",
            HttpStatus.FORBIDDEN
    ),

    ACCOUNT_DISABLED(
            "ERR_AUTH_004",
            "La cuenta de usuario está deshabilitada",
            "Comuníquese con el administrador del sistema.",
            HttpStatus.FORBIDDEN
    ),

    /**
     * Usuarios y roles
     */
    USUARIO_NOT_FOUND(
            "ERR_USU_001",
            "Usuario no encontrado",
            "Verifique que el identificador del usuario exista.",
            HttpStatus.NOT_FOUND
    ),

    USUARIO_DNI_DUPLICADO(
            "ERR_USU_002",
            "El documento del usuario ya está registrado",
            "Verifique el DNI o número de documento ingresado.",
            HttpStatus.CONFLICT
    ),

    USUARIO_CORREO_DUPLICADO(
            "ERR_USU_003",
            "El correo electrónico ya está registrado",
            "Utilice un correo electrónico diferente.",
            HttpStatus.CONFLICT
    ),

    USUARIO_USERNAME_DUPLICADO(
            "ERR_USU_004",
            "El nombre de usuario ya está registrado",
            "Seleccione un nombre de usuario diferente.",
            HttpStatus.CONFLICT
    ),

    ROL_NOT_FOUND(
            "ERR_ROL_001",
            "El rol solicitado no existe",
            "Verifique que el rol esté registrado en el sistema.",
            HttpStatus.NOT_FOUND
    ),

    /**
     * Estudiantes
     */
    ESTUDIANTE_NOT_FOUND(
            "ERR_EST_001",
            "Estudiante no encontrado",
            "Verifique que el ID o código SIAGIE exista en el sistema.",
            HttpStatus.NOT_FOUND
    ),

    ESTUDIANTE_SIAGIE_DUPLICADO(
            "ERR_EST_002",
            "El código SIAGIE ya se encuentra registrado",
            "Verifique el código SIAGIE e ingrese uno que no esté registrado.",
            HttpStatus.CONFLICT
    ),

    ESTUDIANTE_FECHA_NACIMIENTO_INVALIDA(
            "ERR_EST_003",
            "La fecha de nacimiento del estudiante no es válida",
            "Verifique que la fecha de nacimiento sea anterior a la fecha actual.",
            HttpStatus.BAD_REQUEST
    ),

    /**
     * Apoderados y relaciones familiares
     */
    APODERADO_NOT_FOUND(
            "ERR_APO_001",
            "Apoderado no encontrado",
            "Verifique que el ID o número de documento exista en el sistema.",
            HttpStatus.NOT_FOUND
    ),

    APODERADO_DOCUMENTO_DUPLICADO(
            "ERR_APO_002",
            "El documento del apoderado ya está registrado",
            "Verifique el tipo y número de documento ingresados.",
            HttpStatus.CONFLICT
    ),

    RELACION_APODERADO_NOT_FOUND(
            "ERR_APO_003",
            "No se encontró el vínculo entre el estudiante y el apoderado",
            "Verifique que exista una relación registrada entre ambos.",
            HttpStatus.NOT_FOUND
    ),

    RELACION_APODERADO_DUPLICADA(
            "ERR_APO_004",
            "El apoderado ya está vinculado al estudiante",
            "Verifique las relaciones familiares registradas.",
            HttpStatus.CONFLICT
    ),

    /**
     * Tipos de incidencia
     */
    TIPO_INCIDENCIA_NOT_FOUND(
            "ERR_TIP_001",
            "Tipo de incidencia no encontrado",
            "Verifique que el tipo de incidencia exista en el catálogo.",
            HttpStatus.NOT_FOUND
    ),

    TIPO_INCIDENCIA_INACTIVO(
            "ERR_TIP_002",
            "El tipo de incidencia no está habilitado",
            "Seleccione un tipo de incidencia activo.",
            HttpStatus.CONFLICT
    ),

    /**
     * Incidentes
     */
    INCIDENTE_NOT_FOUND(
            "ERR_INC_001",
            "Incidente no encontrado",
            "Verifique el identificador o código del caso.",
            HttpStatus.NOT_FOUND
    ),

    INCIDENTE_CODIGO_DUPLICADO(
            "ERR_INC_002",
            "El código del caso ya está registrado",
            "Genere un código de caso único.",
            HttpStatus.CONFLICT
    ),

    INCIDENTE_ESTADO_INVALIDO(
            "ERR_INC_003",
            "El estado solicitado no es válido",
            "Utilice uno de los estados permitidos para los incidentes.",
            HttpStatus.BAD_REQUEST
    ),

    INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA(
            "ERR_INC_004",
            "No se permite la transición al estado solicitado",
            "Verifique el estado actual del caso y las operaciones permitidas.",
            HttpStatus.CONFLICT
    ),

    INCIDENTE_CERRADO(
            "ERR_INC_005",
            "El incidente está cerrado y no admite esta operación",
            "Verifique el estado del caso o utilice el procedimiento autorizado.",
            HttpStatus.CONFLICT
    ),

    INCIDENTE_RESPONSABLE_NOT_FOUND(
            "ERR_INC_006",
            "No se encontró al responsable del incidente",
            "Verifique que el usuario responsable exista en el sistema.",
            HttpStatus.NOT_FOUND
    ),

    INCIDENTE_SIN_INVOLUCRADOS(
            "ERR_INC_007",
            "El incidente no tiene los involucrados requeridos",
            "Registre los involucrados que correspondan antes de continuar.",
            HttpStatus.CONFLICT
    ),

    /**
     * Involucrados
     */
    INVOLUCRADO_NOT_FOUND(
            "ERR_INV_001",
            "Involucrado no encontrado",
            "Verifique que el involucrado esté registrado en el caso.",
            HttpStatus.NOT_FOUND
    ),

    INVOLUCRADO_DUPLICADO(
            "ERR_INV_002",
            "La persona ya está registrada con ese rol en el incidente",
            "Verifique los involucrados y sus roles en el caso.",
            HttpStatus.CONFLICT
    ),

    INVOLUCRADO_ENTIDAD_INVALIDA(
            "ERR_INV_003",
            "Debe especificar un único tipo de involucrado",
            "Seleccione un estudiante o un usuario del personal, pero no ambos.",
            HttpStatus.BAD_REQUEST
    ),

    /**
     * Seguimiento y acciones
     */
    ACCION_SEGUIMIENTO_NOT_FOUND(
            "ERR_SEG_001",
            "Acción de seguimiento no encontrada",
            "Verifique el identificador de la acción.",
            HttpStatus.NOT_FOUND
    ),

    ACCION_ESTADO_INVALIDO(
            "ERR_SEG_002",
            "El estado de la acción de seguimiento no es válido",
            "Verifique los estados permitidos para las acciones.",
            HttpStatus.BAD_REQUEST
    ),

    ACCION_TRANSICION_NO_PERMITIDA(
            "ERR_SEG_003",
            "No se permite el cambio de estado solicitado para la acción",
            "Verifique el estado actual y las transiciones permitidas.",
            HttpStatus.CONFLICT
    ),

    /**
     * Evidencias y archivos adjuntos
     */
    ADJUNTO_NOT_FOUND(
            "ERR_ADJ_001",
            "Evidencia o archivo adjunto no encontrado",
            "Verifique que el archivo esté asociado al caso.",
            HttpStatus.NOT_FOUND
    ),

    ADJUNTO_TAMANO_EXCEDIDO(
            "ERR_ADJ_002",
            "El archivo supera el tamaño máximo permitido",
            "El tamaño máximo permitido por archivo es de 10 MB.",
            HttpStatus.PAYLOAD_TOO_LARGE
    ),

    ADJUNTO_TIPO_NO_PERMITIDO(
            "ERR_ADJ_003",
            "El tipo de archivo no está permitido",
            "Verifique los formatos admitidos para las evidencias.",
            HttpStatus.UNSUPPORTED_MEDIA_TYPE
    ),

    ADJUNTO_ALMACENAMIENTO_ERROR(
            "ERR_ADJ_004",
            "No fue posible almacenar la evidencia",
            "Intente nuevamente más tarde.",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    /**
     * Notificaciones
     */
    NOTIFICACION_NOT_FOUND(
            "ERR_NOT_001",
            "Notificación no encontrada",
            "Verifique el identificador de la notificación.",
            HttpStatus.NOT_FOUND
    ),

    NOTIFICACION_CANAL_INVALIDO(
            "ERR_NOT_002",
            "El canal de notificación no es válido",
            "Seleccione un canal de comunicación admitido.",
            HttpStatus.BAD_REQUEST
    ),

    NOTIFICACION_ENVIO_ERROR(
            "ERR_NOT_003",
            "No fue posible enviar la notificación",
            "Verifique el estado del servicio de comunicación e intente nuevamente.",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    NOTIFICACION_ESTADO_INVALIDO(
            "ERR_NOT_004",
            "La notificación no admite la transición de estado solicitada",
            "Verifique el estado actual y el flujo de envío.",
            HttpStatus.CONFLICT
    ),

    /**
     * Derivacion s SISEVE
     */
    SISEVE_DERIVACION_INVALIDA(
            "ERR_SIS_001",
            "No se puede realizar la derivación del incidente",
            "Verifique los datos y requisitos necesarios para la derivación.",
            HttpStatus.UNPROCESSABLE_ENTITY
    ),

    SISEVE_SERVICIO_NO_DISPONIBLE(
            "ERR_SIS_002",
            "El servicio externo de SíSeVe no está disponible",
            "Intente nuevamente cuando el servicio esté disponible.",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    SISEVE_RESPUESTA_INVALIDA(
            "ERR_SIS_003",
            "No se pudo interpretar la respuesta del servicio externo",
            "Intente nuevamente más tarde o comuníquese con soporte.",
            HttpStatus.BAD_GATEWAY
    ),

    /**
     * Conflictos de integridad y estado
     */
    OPERACION_NO_PERMITIDA(
            "ERR_NEG_001",
            "La operación no está permitida en el estado actual",
            "Verifique las condiciones necesarias para ejecutar la operación.",
            HttpStatus.CONFLICT
    ),

    RECURSO_EN_USO(
            "ERR_NEG_002",
            "El recurso está siendo utilizado por otros registros",
            "Revise las relaciones existentes antes de modificar o eliminar el recurso.",
            HttpStatus.CONFLICT
    ),

    /**
     * Disponibilidad y errores generales del servidor
     */
    RATE_LIMIT_EXCEEDED(
            "ERR_HTTP_004",
            "Se ha superado el límite de solicitudes permitido",
            "Espere un momento antes de realizar nuevas solicitudes.",
            HttpStatus.TOO_MANY_REQUESTS
    ),

    GATEWAY_TIMEOUT(
            "ERR_SRV_002",
            "Se agotó el tiempo de espera de un servicio externo",
            "Intente nuevamente más tarde.",
            HttpStatus.GATEWAY_TIMEOUT
    ),

    SERVICE_UNAVAILABLE(
            "ERR_SRV_003",
            "El servicio no está disponible temporalmente",
            "Intente nuevamente más tarde.",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    INTERNAL_SERVER_ERROR(
            "ERR_SRV_001",
            "Ocurrió un error interno en el servidor",
            "Intente nuevamente más tarde o comuníquese con soporte."
            ,
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String code;
    private final String message;
    private final String suggestion;
    private final HttpStatus status;

    ErrorCatalog(
            String code,
            String message,
            String suggestion,
            HttpStatus status
    ) {
        this.code = code;
        this.message = message;
        this.suggestion = suggestion;
        this.status = status;
    }
}