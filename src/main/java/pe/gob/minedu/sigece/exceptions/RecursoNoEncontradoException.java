package pe.gob.minedu.sigece.exceptions;

import pe.gob.minedu.sigece.utils.ErrorCatalog;

public class RecursoNoEncontradoException extends DominioException {
    public RecursoNoEncontradoException(ErrorCatalog errorCatalog) { super(errorCatalog); }
    public RecursoNoEncontradoException(ErrorCatalog errorCatalog, String message) { super(errorCatalog, message); }
}