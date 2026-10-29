package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.auth.UsuarioResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.RolResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioRegistroRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioResumenResponseDto;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.models.Rol;
import pe.gob.minedu.sigece.models.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toEntity(UsuarioRegistroRequestDto dto, Rol rol, String passwordHash) {
        Usuario entity = new Usuario();
        entity.setNombres(dto.nombres().trim());
        entity.setApellidos(dto.apellidos().trim());
        entity.setDni(dto.dni().trim());
        entity.setCorreo(dto.correo().trim().toLowerCase());
        entity.setUsername(dto.username().trim());
        entity.setPasswordHash(passwordHash);
        entity.setTelefono(dto.telefono() != null ? dto.telefono().trim() : null);
        entity.setRol(rol);
        entity.setActivo(true);
        return entity;
    }

    /**
     * Mapeo para Auth y Sesión activa (dtos.auth.UsuarioResponseDto)
     */
    public static UsuarioResponseDto toAuthDto(Usuario u) {
        if (u == null) {
            return null;
        }

        String rolString = (u.getRol() != null && u.getRol().getNombre() != null)
                ? u.getRol().getNombre().name()
                : RolNombre.ROLE_INVITADO.name();

        return new UsuarioResponseDto(
                u.getId(),
                u.getNombres(),
                u.getApellidos(),
                u.getUsername(),
                u.getCorreo(),
                rolString,
                u.getActivo(),
                u.getDebeCambiarPassword(),
                u.getUltimoAcceso()
        );
    }

    /**
     * Mapeo para Administración y Mantenimiento (dtos.usuarios.UsuarioMantenimientoResponseDto)
     */
    public static UsuarioMantenimientoResponseDto toDto(Usuario u) {
        return new UsuarioMantenimientoResponseDto(
                u.getId(),
                u.getNombres(),
                u.getApellidos(),
                u.getDni(),
                u.getCorreo(),
                u.getUsername(),
                u.getTelefono(),
                toRolDto(u.getRol()),
                u.getActivo(),
                u.getUltimoAcceso(),
                u.getIntentosFallidos(),
                u.getDebeCambiarPassword(),
                u.getCreatedAt()
        );
    }

    public static RolResponseDto toRolDto(Rol rol) {
        if (rol == null) {
            return null;
        }
        return new RolResponseDto(
                rol.getId(),
                rol.getNombre() != null ? rol.getNombre() : RolNombre.ROLE_INVITADO,
                rol.getDescripcion()
        );
    }

    /**
     * Mapeo resumido para asignaciones
     */
    public static UsuarioResumenResponseDto toResumenDto(Usuario u, int casosActivos) {
        RolNombre rol = (u.getRol() != null && u.getRol().getNombre() != null)
                ? u.getRol().getNombre()
                : RolNombre.ROLE_INVITADO;

        return new UsuarioResumenResponseDto(
                u.getId(),
                u.getNombres() + " " + u.getApellidos(),
                u.getUsername(),
                rol,
                casosActivos
        );
    }
}
