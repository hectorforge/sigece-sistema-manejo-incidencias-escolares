package pe.gob.minedu.sigece.dtos.usuarios;

import pe.gob.minedu.sigece.enums.RolNombre;

public record UsuarioResumenResponseDto(
        Long id,
        String nombreCompleto,
        String username,
        RolNombre rol,
        int casosActivosAsignados
) {
}