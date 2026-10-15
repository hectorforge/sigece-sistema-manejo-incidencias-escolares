package pe.gob.minedu.sigece.exceptions;

import lombok.Getter;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

@Getter
public class DominioException extends RuntimeException {
    private final ErrorCatalog errorCatalog;

    public DominioException(ErrorCatalog errorCatalog) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
    }

    public DominioException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }
}