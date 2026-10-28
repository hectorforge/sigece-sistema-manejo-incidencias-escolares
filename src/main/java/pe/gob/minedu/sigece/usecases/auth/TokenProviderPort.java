package pe.gob.minedu.sigece.usecases.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.security.UserPrincipal;
import pe.gob.minedu.sigece.security.jwt.JwtUtils;

@Service
@RequiredArgsConstructor
public class TokenProviderPort implements ITokenProviderPort{

    private final JwtUtils jwtUtils;

    @Override
    public String generarAccessToken(Usuario usuario) {
        UserPrincipal principal = UserPrincipal.build(usuario);
        return jwtUtils.generarAccessToken(principal);
    }

    @Override
    public String generarRefreshToken(Usuario usuario) {
        UserPrincipal principal = UserPrincipal.build(usuario);
        return jwtUtils.generarRefreshToken(principal);
    }

    @Override
    public Long obtenerTiempoExpiracionSegundos() {
        return 0L;
    }

    @Override
    public boolean validarToken(String token) {
        return jwtUtils.validarToken(token);
    }

    @Override
    public String extraerUsername(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    @Override
    public Long extraerUsuarioId(String token) {
        return 0L;
    }
}
