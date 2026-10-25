package pe.gob.minedu.sigece.dtos.usuarios;

import pe.gob.minedu.sigece.enums.RolNombre;

public record RolResponseDto(
        Long id,
        RolNombre nombre,
        String descripcion
) {
}