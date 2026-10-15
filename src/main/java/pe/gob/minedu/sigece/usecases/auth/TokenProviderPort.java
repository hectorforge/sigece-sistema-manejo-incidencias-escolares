package pe.gob.minedu.sigece.usecases.auth;

import org.springframework.stereotype.Service;
import pe.gob.minedu.sigece.models.Usuario;

@Service
public class TokenProviderPort implements ITokenProviderPort{
    @Override
    public String generarAccessToken(Usuario usuario) {
        return "";
    }

    @Override
    public String generarRefreshToken(Usuario usuario) {
        return "";
    }

    @Override
    public Long obtenerTiempoExpiracionSegundos() {
        return 0L;
    }

    @Override
    public boolean validarToken(String token) {
        return false;
    }

    @Override
    public String extraerUsername(String token) {
        return "";
    }

    @Override
    public Long extraerUsuarioId(String token) {
        return 0L;
    }
}
