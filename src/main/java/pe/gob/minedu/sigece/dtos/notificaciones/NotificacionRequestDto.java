package pe.gob.minedu.sigece.dtos.notificaciones;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.CanalNotificacion;

public record NotificacionRequestDto(
        @NotNull(message = "El apoderado destinatario es obligatorio")
        Long apoderadoId,

        Long alertaId,

        @NotNull(message = "El canal de comunicación es obligatorio")
        CanalNotificacion canal,

        @Size(max = 150, message = "El destino no debe superar los 150 caracteres")
        String destino, // Teléfono o correo del envío

        @NotBlank(message = "El asunto es obligatorio")
        @Size(max = 150, message = "El asunto no debe superar los 150 caracteres")
        String asunto,

        @NotBlank(message = "El cuerpo del mensaje es obligatorio")
        String mensaje
) {
}