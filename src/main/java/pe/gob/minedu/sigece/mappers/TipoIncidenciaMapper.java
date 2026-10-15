package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaRequestDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaResponseDto;
import pe.gob.minedu.sigece.models.TipoIncidencia;

public final class TipoIncidenciaMapper {
    private TipoIncidenciaMapper() {}

    public static TipoIncidencia toEntity(TipoIncidenciaRequestDto dto) {
        TipoIncidencia entity = new TipoIncidencia();
        entity.setNombre(dto.nombre().trim());
        entity.setCategoriaSiseve(dto.categoriaSiseve());
        entity.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
        entity.setActivo(true);
        return entity;
    }

    public static TipoIncidenciaResponseDto toDto(TipoIncidencia entity) {
        return new TipoIncidenciaResponseDto(
                entity.getId(),
                entity.getNombre(),
                entity.getCategoriaSiseve(),
                entity.getDescripcion(),
                entity.getActivo()
        );
    }
}