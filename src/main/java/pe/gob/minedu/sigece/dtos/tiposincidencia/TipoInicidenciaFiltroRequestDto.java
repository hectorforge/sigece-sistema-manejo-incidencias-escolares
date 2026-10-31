package pe.gob.minedu.sigece.dtos.tiposincidencia;

import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.enums.Gravedad;

public record TipoInicidenciaFiltroRequestDto(
    String termino,
    CategoriaSiseve categoriaSiseve,
    Gravedad gravedadBase,
    Integer page,
    Integer size
) {
}
