package pe.gob.minedu.sigece.dtos.usuarios;

public record UsuarioResumenResponseDto(
        Long id,
        String nombreCompleto,
        String username,
        String rol,
        int casosActivosAsignados
) {
}