package pe.gob.minedu.sigece.dtos.apoderados;

import pe.gob.minedu.sigece.enums.TipoDocumento;

public record ApoderadoFiltroRequestDto(
        String termino,
        TipoDocumento tipoDocumento,
        Integer page,
        Integer size
) {
}
