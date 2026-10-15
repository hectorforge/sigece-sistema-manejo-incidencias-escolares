package pe.gob.minedu.sigece.exceptions;

import pe.gob.minedu.sigece.utils.ErrorCatalog;

public class OperacionInvalidaException extends DominioException {
    public OperacionInvalidaException(ErrorCatalog errorCatalog) { super(errorCatalog); }
    public OperacionInvalidaException(ErrorCatalog errorCatalog, String message) { super(errorCatalog, message); }
}