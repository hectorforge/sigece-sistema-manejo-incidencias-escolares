package pe.gob.minedu.sigece.dtos.auth;

import java.time.OffsetDateTime;

public record UsuarioResponseDto(
        Long id,
        String nombres,
        String apellidos,
        String username,
        String correo,
        String rol,
        Boolean activo,
        Boolean debeCambiarPassword,
        OffsetDateTime ultimoAcceso
) {
}