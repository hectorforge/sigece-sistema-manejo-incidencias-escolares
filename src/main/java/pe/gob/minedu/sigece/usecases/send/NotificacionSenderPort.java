package pe.gob.minedu.sigece.usecases.send;

import org.springframework.stereotype.Service;
import pe.gob.minedu.sigece.enums.CanalNotificacion;

@Service
public class NotificacionSenderPort implements INotificacionSenderPort{
    @Override
    public ResultadoEnvioPort enviar(CanalNotificacion canal, String destino, String asunto, String mensaje) {
        return null;
    }
}
