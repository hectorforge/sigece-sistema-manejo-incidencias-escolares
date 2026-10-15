package pe.gob.minedu.sigece.dtos.estudiantes;

import pe.gob.minedu.sigece.enums.Nivel;

public record EstudianteFiltroRequestDto(
        String termino,
        Nivel nivel,
        String grado,
        String seccion,
        Boolean activo,
        Integer page,
        Integer size
) {
}