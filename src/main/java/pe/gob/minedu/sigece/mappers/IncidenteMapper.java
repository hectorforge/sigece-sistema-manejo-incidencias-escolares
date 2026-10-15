package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.incidentes.IncidenteResponseDto;
import pe.gob.minedu.sigece.dtos.incidentes.IncidenteResumenResponseDto;
import pe.gob.minedu.sigece.models.Incidente;

public final class IncidenteMapper {
    private IncidenteMapper() {}

    public static IncidenteResponseDto toDto(Incidente i) {
        return new IncidenteResponseDto(
                i.getId(),
                i.getCodigoCaso(),
                i.getTipoIncidencia().getId(),
                i.getTipoIncidencia().getNombre(),
                i.getGravedad(),
                i.getDescripcion(),
                i.getLugarOcurrencia(),
                i.getFechaOcurrencia(),
                i.getFechaRegistro(),
                i.getEstado(),
                i.getReportadoPor().getId(),
                i.getReportadoPor().getNombres() + " " + i.getReportadoPor().getApellidos(),
                i.getResponsableActual() != null ? i.getResponsableActual().getId() : null,
                i.getResponsableActual() != null ? i.getResponsableActual().getNombres() + " " + i.getResponsableActual().getApellidos() : null,
                i.getReincidencia(),
                i.getActaCierre(),
                i.getFechaCierre(),
                i.getCerradoPor() != null ? i.getCerradoPor().getNombres() + " " + i.getCerradoPor().getApellidos() : null,
                i.getReportadoSiseve(),
                i.getEstadoDerivacion(),
                i.getModoDerivacion(),
                i.getCodigoSiseve(),
                i.getCreatedAt()
        );
    }

    public static IncidenteResumenResponseDto toResumenDto(Incidente i) {
        return new IncidenteResumenResponseDto(
                i.getId(),
                i.getCodigoCaso(),
                i.getTipoIncidencia().getNombre(),
                i.getGravedad(),
                i.getLugarOcurrencia(),
                i.getFechaOcurrencia(),
                i.getEstado(),
                i.getResponsableActual() != null ? i.getResponsableActual().getNombres() + " " + i.getResponsableActual().getApellidos() : "Sin Asignar",
                i.getReincidencia()
        );
    }
}