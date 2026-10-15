package pe.gob.minedu.sigece.dtos.alertas;

import java.time.OffsetDateTime;

public record ChequeoAlertasResponseDto(
        int alertasGeneradas,
        int casosDemoradosDetectados,
        int reincidenciasDetectadas,
        int seguimientosVencidosDetectados,
        OffsetDateTime fechaEjecucion
) {
}