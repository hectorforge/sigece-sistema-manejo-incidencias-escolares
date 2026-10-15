package pe.gob.minedu.sigece.dtos.usuarios;

import java.time.OffsetDateTime;

public record UsuarioResponseDto(
        Long id,
        String nombres,
        String apellidos,
        String dni,
        String correo,
        String username,
        String telefono,
        RolResponseDto rol,
        Boolean activo,
        OffsetDateTime ultimoAcceso,
        Integer intentosFallidos,
        Boolean debeCambiarPassword,
        OffsetDateTime createdAt
) {
}