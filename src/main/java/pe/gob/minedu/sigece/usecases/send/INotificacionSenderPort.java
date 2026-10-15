package pe.gob.minedu.sigece.usecases.send;

import pe.gob.minedu.sigece.enums.CanalNotificacion;

public interface INotificacionSenderPort {
    ResultadoEnvioPort enviar(CanalNotificacion canal, String destino, String asunto, String mensaje);

    record ResultadoEnvioPort(boolean exitoso, String transaccionId, String detalleRespuesta) {}
}