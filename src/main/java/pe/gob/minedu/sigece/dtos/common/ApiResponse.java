package pe.gob.minedu.sigece.dtos.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        ApiError error,
        String path,
        String traceId,
        PaginationMeta meta,
        OffsetDateTime timestamp
) {

    public static <T> ApiResponse<T> success(
            T data,
            String message,
            HttpStatus status,
            String path,
            String traceId
    ) {
        return new ApiResponse<>(
                true,
                status.value(),
                message,
                data,
                null,
                path,
                traceId,
                null,
                OffsetDateTime.now()
        );
    }

    public static <T> ApiResponse<T> success(
            T data,
            String message
    ) {
        return success(
                data,
                message,
                HttpStatus.OK,
                null,
                null
        );
    }

    public static <T> ApiResponse<T> paginated(
            T data,
            PaginationMeta meta,
            String message,
            String path,
            String traceId
    ) {
        return new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                message,
                data,
                null,
                path,
                traceId,
                meta,
                OffsetDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(
            HttpStatus status,
            ErrorCatalog catalog,
            String path,
            String traceId
    ) {
        return new ApiResponse<>(
                false,
                status.value(),
                catalog.getMessage(),
                null,
                ApiError.fromCatalog(catalog),
                path,
                traceId,
                null,
                OffsetDateTime.now()
        );
    }

    public static <T> ApiResponse<T> validationError(
            Map<String, String> validationErrors,
            String path,
            String traceId
    ) {
        return new ApiResponse<>(
                false,
                HttpStatus.BAD_REQUEST.value(),
                ErrorCatalog.VALIDATION_ERROR.getMessage(),
                null,
                ApiError.fromCatalog(
                        ErrorCatalog.VALIDATION_ERROR,
                        validationErrors
                ),
                path,
                traceId,
                null,
                OffsetDateTime.now()
        );
    }
}