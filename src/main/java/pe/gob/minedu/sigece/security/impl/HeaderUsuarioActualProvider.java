package pe.gob.minedu.sigece.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

@Component
public class HeaderUsuarioActualProvider implements IUsuarioActualProvider {

    private static final String HEADER = "X-Usuario-Id";

    @Override
    public Long obtenerId(HttpServletRequest request) {
        String valor = request.getHeader(HEADER);
        if (valor == null || !valor.matches("\\d+")) {
            throw new OperacionInvalidaException(ErrorCatalog.BAD_REQUEST,
                    "Falta el header " + HEADER + " (modo desarrollo sin seguridad)");
        }
        return Long.parseLong(valor);
    }


}
