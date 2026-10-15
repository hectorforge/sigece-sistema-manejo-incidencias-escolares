package pe.gob.minedu.sigece.dtos.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String code,
        String message,
        String suggestion,
        Map<String, String> details
) {

    public static ApiError fromCatalog(ErrorCatalog catalog) {
        return new ApiError(
                catalog.getCode(),
                catalog.getMessage(),
                catalog.getSuggestion(),
                null
        );
    }

    public static ApiError fromCatalog(
            ErrorCatalog catalog,
            String customMessage
    ) {
        return new ApiError(
                catalog.getCode(),
                customMessage,
                catalog.getSuggestion(),
                null
        );
    }

    public static ApiError fromCatalog(
            ErrorCatalog catalog,
            Map<String, String> details
    ) {
        return new ApiError(
                catalog.getCode(),
                catalog.getMessage(),
                catalog.getSuggestion(),
                details
        );
    }
}