package pe.gob.minedu.sigece.dtos.alertas;

import pe.gob.minedu.sigece.enums.EstadoAlerta;
import pe.gob.minedu.sigece.enums.OrigenAlerta;
import pe.gob.minedu.sigece.enums.TipoAlerta;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AlertaResponseDto(
        Long id,
        UUID incidenteId,
        String codigoCaso,
        Long estudianteId,
        String estudianteNombre,
        Long usuarioDestinoId,
        TipoAlerta tipoAlerta,
        OrigenAlerta origen,
        String mensaje,
        String parametros,
        EstadoAlerta estadoAlerta,
        OffsetDateTime fechaGeneracion,
        OffsetDateTime fechaResolucion,
        String resueltaPorNombre,
        String observacionResolucion
) {
}