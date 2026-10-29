package pe.gob.minedu.sigece.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.gob.minedu.sigece.exceptions.DominioException;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.security.UserPrincipal;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.Optional;

@Component
public class SecurityContextUsuarioActualProvider implements IUsuarioActualProvider {

    @Override
    public Long getUsuarioActualId() {
        return obtenerPrincipal()
                .map(UserPrincipal::getId)
                .orElseThrow(() -> new DominioException(ErrorCatalog.USUARIO_ACTUAL_NO_IDENTIFICADO));
    }

    @Override
    public Long obtenerId(HttpServletRequest request) {

        Optional<UserPrincipal> principal = obtenerPrincipal();
        if (principal.isPresent()) {
            return principal.get().getId();
        }

        if (request != null) {
            String headerId = request.getHeader("X-Usuario-Id");
            if (headerId != null && !headerId.isBlank()) {
                try {
                    return Long.parseLong(headerId.trim());
                } catch (NumberFormatException e) {
                    throw new DominioException(ErrorCatalog.HEADER_USUARIO_INVALIDO);
                }
            }
        }

        throw new DominioException(ErrorCatalog.USUARIO_ACTUAL_NO_IDENTIFICADO);
    }

    @Override
    public String getUsernameActual() {
        return obtenerPrincipal()
                .map(UserPrincipal::getUsername)
                .orElseThrow(() -> new DominioException(ErrorCatalog.USUARIO_ACTUAL_NO_IDENTIFICADO));
    }

    @Override
    public String getRolActual() {
        return obtenerPrincipal()
                .map(UserPrincipal::getRolNombre)
                .orElseThrow(() -> new DominioException(ErrorCatalog.USUARIO_ACTUAL_NO_IDENTIFICADO));
    }

    private Optional<UserPrincipal> obtenerPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }
}
