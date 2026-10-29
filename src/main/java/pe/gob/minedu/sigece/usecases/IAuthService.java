package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.auth.AuthResponseDto;
import pe.gob.minedu.sigece.dtos.auth.LoginRequestDto;
import pe.gob.minedu.sigece.dtos.auth.RefreshTokenRequestDto;
import pe.gob.minedu.sigece.dtos.auth.UsuarioResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;

/**
 * Servicio encargado de la autenticación y del ciclo de vida de la sesión (JWT + Refresh Token).
 * Casos de uso asociados: CU01, CU02.
 */
public interface IAuthService {

    /**
     * Autentica a un usuario en el sistema validando sus credenciales de acceso.
     * <p>Caso de uso: <b>CU01 · Iniciar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param dto Credenciales de acceso del usuario (username y password).
     * @return Par de tokens generados (Access Token y Refresh Token).
     */
    AuthResponseDto autenticar(LoginRequestDto dto);

    /**
     * Genera un nuevo Access Token a partir de un Refresh Token válido y no expirado.
     * <p>Caso de uso: <b>CU01 · Iniciar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param dto Refresh token vigente del usuario.
     * @return Nuevo par de tokens de autenticación.
     */
    AuthResponseDto refrescarToken(RefreshTokenRequestDto dto);

    /**
     * Invalida el Refresh Token proporcionado cerrando la sesión del usuario.
     * <p>Caso de uso: <b>CU02 · Cambiar contraseña / cerrar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param dto Refresh token a revocar/invalidar.
     */
    void cerrarSesion(RefreshTokenRequestDto dto);

    /**
     * Obtiene los datos del usuario correspondiente a la sesión activa en curso.
     * <p>Caso de uso: <b>CU01 · Iniciar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param usuarioId Identificador del usuario autenticado extraído del token JWT.
     * @return Datos del perfil del usuario autenticado.
     */
    UsuarioMantenimientoResponseDto obtenerSesionActual(Long usuarioId);
}
