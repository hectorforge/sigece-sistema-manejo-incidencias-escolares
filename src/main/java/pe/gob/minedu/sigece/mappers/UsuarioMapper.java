package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.usuarios.RolResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioRegistroRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioResponseDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioResumenResponseDto;
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

    public static UsuarioResponseDto toDto(Usuario u) {
        return new UsuarioResponseDto(
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
        return new RolResponseDto(
                rol.getId(),
                rol.getNombre(),
                rol.getDescripcion()
        );
    }

    public static UsuarioResumenResponseDto toResumenDto(Usuario u, int casosActivos) {
        return new UsuarioResumenResponseDto(
                u.getId(),
                u.getNombres() + " " + u.getApellidos(),
                u.getUsername(),
                u.getRol().getNombre(),
                casosActivos
        );
    }
}