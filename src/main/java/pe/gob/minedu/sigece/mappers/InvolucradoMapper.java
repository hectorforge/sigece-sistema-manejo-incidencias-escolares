package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoResponseDto;
import pe.gob.minedu.sigece.models.IncidenteInvolucrado;

public final class InvolucradoMapper {
    private InvolucradoMapper() {}

    public static InvolucradoResponseDto toDto(IncidenteInvolucrado ii) {
        boolean esEstudiante = ii.getEstudiante() != null;
        String nombre = esEstudiante
                ? ii.getEstudiante().getNombres() + " " + ii.getEstudiante().getApellidos()
                : ii.getUsuario().getNombres() + " " + ii.getUsuario().getApellidos();
        String doc = esEstudiante ? ii.getEstudiante().getCodigoSiagie() : ii.getUsuario().getDni();

        return new InvolucradoResponseDto(
                ii.getId(),
                ii.getIncidente().getId(),
                esEstudiante ? ii.getEstudiante().getId() : null,
                !esEstudiante ? ii.getUsuario().getId() : null,
                nombre,
                doc,
                esEstudiante ? "ESTUDIANTE" : "PERSONAL",
                ii.getRolInvolucrado(),
                ii.getObservaciones(),
                ii.getCreatedAt()
        );
    }
}