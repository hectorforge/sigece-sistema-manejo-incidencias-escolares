package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.RolNombre;
import pe.edu.cibertec.sigece.service.dto.UsuarioDTO;

import java.util.List;

/**
 * Administra al personal de la IE (docentes, tutores, psicólogos,
 * miembros del Comité de Gestión del Bienestar y directivos) con
 * acceso al sistema, bajo el modelo de control de acceso RBAC.
 */
public interface UsuarioService {

    UsuarioDTO registrarUsuario(UsuarioDTO dto, String passwordPlano);

    UsuarioDTO actualizarPerfil(Long usuarioId, UsuarioDTO dto);

    void cambiarPassword(Long usuarioId, String passwordActual, String passwordNuevo);

    void desactivarUsuario(Long usuarioId);

    void reactivarUsuario(Long usuarioId);

    UsuarioDTO obtenerPorId(Long usuarioId);

    UsuarioDTO obtenerPorUsername(String username);

    /**
     * Lista el personal habilitado para ser asignado como responsable
     * de un caso, filtrado por rol (p. ej. solo ROLE_PSICOLOGO para
     * derivaciones de salud mental).
     */
    List<UsuarioDTO> listarPorRol(RolNombre rol);

    /**
     * Carga de trabajo actual: cantidad de incidentes activos
     * (no CERRADO) asignados como responsable_actual a un usuario,
     * para balancear la asignación de nuevos casos.
     */
    int contarCasosActivosAsignados(Long usuarioId);
}