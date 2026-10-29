package pe.gob.minedu.sigece.dtos.tiposincidencia;

import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.enums.Gravedad;

public record TipoIncidenciaResponseDto(
        Long id,
        String nombre,
        CategoriaSiseve categoriaSiseve,
        Gravedad gravedadBase,
        String descripcion,
        Boolean activo
) {
}