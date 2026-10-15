package pe.gob.minedu.sigece.exceptions;

import lombok.Getter;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

@Getter
public class ApoderadoNoEncontradoException extends RuntimeException {

    private final ErrorCatalog errorCatalog;

    public ApoderadoNoEncontradoException(ErrorCatalog errorCatalog) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
    }

    public ApoderadoNoEncontradoException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }
}
