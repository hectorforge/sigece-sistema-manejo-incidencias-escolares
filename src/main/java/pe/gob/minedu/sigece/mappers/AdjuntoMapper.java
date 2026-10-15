package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.adjuntos.AdjuntoResponseDto;
import pe.gob.minedu.sigece.models.IncidenteAdjunto;

public final class AdjuntoMapper {
    private AdjuntoMapper() {}

    public static AdjuntoResponseDto toDto(IncidenteAdjunto ia) {
        return new AdjuntoResponseDto(
                ia.getId(),
                ia.getIncidente().getId(),
                ia.getAccion() != null ? ia.getAccion().getId() : null,
                ia.getSubidoPor().getId(),
                ia.getSubidoPor().getNombres() + " " + ia.getSubidoPor().getApellidos(),
                ia.getNombreOriginal(),
                ia.getDescripcion(),
                ia.getTipoMime(),
                ia.getTamanoBytes(),
                ia.getUrlStorage(),
                ia.getHashSha256(),
                ia.getActivo(),
                ia.getCreatedAt()
        );
    }
}