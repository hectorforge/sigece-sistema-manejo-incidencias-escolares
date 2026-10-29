package pe.gob.minedu.sigece.security;

import jakarta.servlet.http.HttpServletRequest;

public interface IUsuarioActualProvider {
    Long obtenerId(HttpServletRequest request);
    Long getUsuarioActualId();
    String getUsernameActual();
    String getRolActual();
}
