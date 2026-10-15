package pe.gob.minedu.sigece.usecases.auth;

import pe.gob.minedu.sigece.models.Usuario;

public interface ITokenProviderPort {
    String generarAccessToken(Usuario usuario);
    String generarRefreshToken(Usuario usuario);
    Long obtenerTiempoExpiracionSegundos();
    boolean validarToken(String token);
    String extraerUsername(String token);
    Long extraerUsuarioId(String token);
}