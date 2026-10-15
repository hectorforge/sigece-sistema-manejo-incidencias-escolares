package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.auth.CambioPasswordRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.RolResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioRegistroRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioResumenResponseDto;
import pe.gob.minedu.sigece.enums.RolNombre;

import java.util.List;

/**
 * Servicio encargado de la administración del personal de la IE bajo el modelo RBAC.
 * Casos de uso asociados: CU01, CU02, CU03, CU11.
 */
public interface IUsuarioService {

    /**
     * Registra un nuevo usuario en la plataforma asignándole sus credenciales y roles.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param dto Datos del nuevo usuario institucional.
     * @return Información del usuario creado.
     */
    UsuarioResponseDto registrarUsuario(UsuarioRegistroRequestDto dto);

    /**
     * Actualiza los datos informativos y perfil de un usuario existente.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param usuarioId Identificador único del usuario a actualizar.
     * @param dto       Nuevos datos de perfil del usuario.
     * @return Datos actualizados del usuario.
     */
    UsuarioResponseDto actualizarPerfil(Long usuarioId, UsuarioRequestDto dto);

    /**
     * Permite a un usuario modificar su contraseña de acceso actual.
     * <p>Caso de uso: <b>CU02 · Cambiar contraseña / cerrar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param usuarioId Identificador del usuario que solicita el cambio.
     * @param dto       Contraseña actual y nueva contraseña confirmada.
     */
    void cambiarPassword(Long usuarioId, CambioPasswordRequestDto dto);

    /**
     * Restablece la contraseña de un usuario a un valor temporal o genera un link de recuperación.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param usuarioId Identificador del usuario al cual se le restablecerá la contraseña.
     */
    void restablecerPassword(Long usuarioId);

    /**
     * Desactiva lógicamente la cuenta de un usuario impidiendo su acceso al sistema.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param usuarioId Identificador del usuario a desactivar.
     */
    void desactivarUsuario(Long usuarioId);

    /**
     * Reactiva una cuenta previamente inhabilitada.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param usuarioId Identificador del usuario a reactivar.
     */
    void reactivarUsuario(Long usuarioId);

    /**
     * Obtiene el detalle de un usuario por su identificador primario.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param usuarioId Identificador del usuario.
     * @return Datos del usuario consultado.
     */
    UsuarioResponseDto obtenerPorId(Long usuarioId);

    /**
     * Obtiene un usuario a partir de su nombre de usuario (username/DNI).
     * <p>Caso de uso: <b>CU01 · Iniciar sesión</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param username Nombre de usuario o identificador institucional.
     * @return Datos del usuario encontrado.
     */
    UsuarioResponseDto obtenerPorUsername(String username);

    /**
     * Realiza una búsqueda paginada y filtrada de usuarios.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param filtro Criterios de búsqueda y datos de paginación.
     * @return Página con los usuarios encontrados.
     */
    PagedResponse<UsuarioResponseDto> buscar(UsuarioFiltroRequestDto filtro);

    /**
     * Lista los usuarios activos que poseen un rol determinado (ej. TUTOR, PSICOLOGO).
     * <p>Caso de uso: <b>CU11 · Asignar responsable</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param rol Rol por el cual filtrar.
     * @return Lista resumida de usuarios que cumplen con el rol.
     */
    List<UsuarioResumenResponseDto> listarPorRol(RolNombre rol);

    /**
     * Cuenta la cantidad de casos/expedientes activos que tiene asignados un usuario como responsable.
     * <p>Caso de uso: <b>CU11 · Asignar responsable</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param usuarioId Identificador del usuario a consultar.
     * @return Número de casos activos asignados.
     */
    int contarCasosActivosAsignados(Long usuarioId);

    /**
     * Lista todos los roles disponibles en el sistema de seguridad.
     * <p>Caso de uso: <b>CU03 · Gestionar usuarios y roles</b></p>
     * <p>Rol: Administrador</p>
     *
     * @return Lista de roles del catálogo del sistema.
     */
    List<RolResponseDto> listarRoles();
}