package pe.gob.minedu.sigece.dtos.tiposincidencia;

import pe.gob.minedu.sigece.enums.CategoriaSiseve;

public record TipoIncidenciaResponseDto(
        Long id,
        String nombre,
        CategoriaSiseve categoriaSiseve,
        String descripcion,
        Boolean activo
) {
}