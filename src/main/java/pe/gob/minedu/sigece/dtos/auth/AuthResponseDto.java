package pe.gob.minedu.sigece.dtos.auth;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UsuarioResponseDto usuario
) {
}