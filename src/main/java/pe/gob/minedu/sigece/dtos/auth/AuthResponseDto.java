package pe.gob.minedu.sigece.dtos.auth;

import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UsuarioMantenimientoResponseDto usuario
) {
}
