package pe.gob.minedu.sigece.dtos.notificaciones;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.EstadoEnvio;

public record EstadoEnvioRequestDto(
        @NotNull(message = "El nuevo estado de envío es obligatorio")
        EstadoEnvio estadoEnvio,

        @Size(max = 500, message = "El detalle de resultado no debe superar los 500 caracteres")
        String detalleResultado,

        @Size(max = 150, message = "El ID de transacción no debe superar los 150 caracteres")
        String idTransaccionExterno
) {
}