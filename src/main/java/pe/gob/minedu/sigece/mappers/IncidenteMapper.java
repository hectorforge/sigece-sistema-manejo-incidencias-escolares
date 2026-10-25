package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.incidentes.IncidenteResponseDto;
import pe.gob.minedu.sigece.dtos.incidentes.IncidenteResumenResponseDto;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;

public final class IncidenteMapper {
    private IncidenteMapper() {}

    private static String nombre(Usuario u) {
        return u == null ? null : u.getNombres() + " " + u.getApellidos();
    }

    public static IncidenteResponseDto toDto(Incidente i) {
        return new IncidenteResponseDto(
                i.getId(), i.getCodigoCaso(),
                i.getTipoIncidencia().getId(), i.getTipoIncidencia().getNombre(),
                i.getGravedad(), i.getDescripcion(), i.getLugarOcurrencia(),
                i.getMedioOcurrencia(), i.getFechaOcurrencia(), i.getFechaRegistro(),
                i.getEstado(), i.getNotificarApoderado(),
                i.getReportadoPor().getId(), nombre(i.getReportadoPor()),
                i.getResponsableActual().getId(), nombre(i.getResponsableActual()),
                i.getFechaResolucion(), nombre(i.getResueltoPor()),
                i.getActaCierre(), i.getConclusionesFinales(), i.getFechaCierre(), nombre(i.getCerradoPor()),
                i.getReportadoSiseve(), i.getFechaReporteSiseve(), i.getCodigoSiseve(),
                i.getObservacionesSiseve(), nombre(i.getDerivadoPor()),
                i.getCreatedAt());
    }

    public static IncidenteResumenResponseDto toResumenDto(Incidente i) {
        return new IncidenteResumenResponseDto(
                i.getId(), i.getCodigoCaso(), i.getTipoIncidencia().getNombre(), i.getGravedad(),
                i.getLugarOcurrencia(), i.getFechaOcurrencia(), i.getEstado(),
                nombre(i.getResponsableActual()));
    }
}
