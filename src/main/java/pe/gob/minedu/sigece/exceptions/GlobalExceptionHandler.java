package pe.gob.minedu.sigece.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RecursoNoEncontradoException.class, ApoderadoNoEncontradoException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.warn("[{}] Recurso no encontrado: {}", traceId, ex.getMessage());

        ErrorCatalog catalog = (ex instanceof DominioException de) ? de.getErrorCatalog()
                : ((ApoderadoNoEncontradoException) ex).getErrorCatalog();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, catalog, ex.getMessage(), request.getRequestURI(), traceId));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecursoDuplicado(RecursoDuplicadoException ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.warn("[{}] Conflicto por duplicidad: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, ex.getErrorCatalog(), ex.getMessage(), request.getRequestURI(), traceId));
    }

    @ExceptionHandler(OperacionInvalidaException.class)
    public ResponseEntity<ApiResponse<Void>> handleOperacionInvalida(OperacionInvalidaException ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.warn("[{}] Operación inválida: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getErrorCatalog(), ex.getMessage(), request.getRequestURI(), traceId));
    }

    @ExceptionHandler(DominioException.class)
    public ResponseEntity<ApiResponse<Void>> handleDominioException(DominioException ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.warn("[{}] Error de dominio general: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getErrorCatalog(), ex.getMessage(), request.getRequestURI(), traceId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("[{}] Error de validación en la petición: {}", traceId, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(errors, request.getRequestURI(), traceId));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequestFormats(Exception ex, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.warn("[{}] Petición HTTP mal formada o parámetro incompatible: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST,
                        ErrorCatalog.VALIDATION_ERROR,
                        "El cuerpo de la solicitud no tiene el formato JSON esperado o contiene tipos de datos incorrectos.",
                        request.getRequestURI(),
                        traceId
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerError(Exception ex, HttpServletRequest request) {
        String traceId = getTraceId(request);

        log.error("[{}] Error no controlado en el servidor: ", traceId, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        false,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Ha ocurrido un error interno no controlado. Por favor reporte este problema indicando el traceId.",
                        null,
                        new pe.gob.minedu.sigece.dtos.common.ApiError(
                                "INTERNAL_SERVER_ERROR",
                                "Ocurrió un error inesperado al procesar la solicitud.",
                                "Comuníquese con el soporte técnico de MINEDU proporcionando el identificador del incidente.",
                                null
                        ),
                        request.getRequestURI(),
                        traceId,
                        null,
                        java.time.OffsetDateTime.now()
                ));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        if (traceId == null || traceId.isBlank()) {
            traceId = request.getHeader("X-Trace-Id");
        }
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}