package pe.gob.minedu.sigece.exceptions;

import pe.gob.minedu.sigece.utils.ErrorCatalog;

public class RecursoDuplicadoException extends DominioException {
    public RecursoDuplicadoException(ErrorCatalog errorCatalog) { super(errorCatalog); }
    public RecursoDuplicadoException(ErrorCatalog errorCatalog, String message) { super(errorCatalog, message); }
}