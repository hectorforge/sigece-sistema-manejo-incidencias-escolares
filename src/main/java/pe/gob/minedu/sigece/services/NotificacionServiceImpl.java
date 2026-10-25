package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.notificaciones.*;
import pe.gob.minedu.sigece.enums.EstadoEnvio;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.NotificacionMapper;
import pe.gob.minedu.sigece.models.Apoderado;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Notificacion;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IApoderadoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.INotificacionRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.INotificacionService;
import pe.gob.minedu.sigece.usecases.send.INotificacionSenderPort;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements INotificacionService {

    private final INotificacionRepository notificacionRepository;
    private final IIncidenteRepository incidenteRepository;
    private final IApoderadoRepository apoderadoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final INotificacionSenderPort notificacionSender;

    @Override
    @Transactional
    public NotificacionEnvioResponseDto notificarApoderados(UUID incidenteId, NotificacionRequestDto dto, Long usuarioId) {
        log.info("Enviando notificación por canal {} al apoderado ID: {}", dto.canal(), dto.apoderadoId());

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        Apoderado apoderado = apoderadoRepository.findById(dto.apoderadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));

        Usuario remitente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        String destino = dto.destino() != null ? dto.destino().trim() : apoderado.getTelefono();

        var resultadoPort = notificacionSender.enviar(dto.canal(), destino, dto.asunto(), dto.mensaje());

        Notificacion notif = new Notificacion();
        notif.setIncidente(incidente);
        notif.setApoderado(apoderado);
        notif.setEnviadoPor(remitente);
        notif.setCanal(dto.canal());
        notif.setDestino(destino);
        notif.setAsunto(dto.asunto().trim());
        notif.setMensaje(dto.mensaje().trim());
        notif.setIntentosEnvio(1);
        notif.setFechaEnvio(OffsetDateTime.now());
        notif.setEstadoEnvio(resultadoPort.exitoso() ? EstadoEnvio.ENVIADO : EstadoEnvio.FALLIDO);
        notif.setIdTransaccionExterno(resultadoPort.transaccionId());
        notif.setDetalleResultado(resultadoPort.detalleRespuesta());

        Notificacion guardada = notificacionRepository.save(notif);

        return new NotificacionEnvioResponseDto(
                guardada.getId(),
                guardada.getEstadoEnvio(),
                resultadoPort.exitoso(),
                resultadoPort.detalleRespuesta(),
                resultadoPort.transaccionId(),
                guardada.getFechaEnvio()
        );
    }

    @Override
    @Transactional
    public NotificacionResponseDto actualizarEstadoEnvio(Long notificacionId, EstadoEnvioRequestDto dto) {
        Notificacion notif = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.NOTIFICACION_NOT_FOUND));

        notif.setEstadoEnvio(dto.estadoEnvio());
        if (dto.detalleResultado() != null) notif.setDetalleResultado(dto.detalleResultado());
        if (dto.idTransaccionExterno() != null) notif.setIdTransaccionExterno(dto.idTransaccionExterno());

        return NotificacionMapper.toDto(notificacionRepository.save(notif));
    }

    @Override
    @Transactional
    public NotificacionEnvioResponseDto reintentarEnvio(Long notificacionId, Long usuarioId) {
        Notificacion notif = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.NOTIFICACION_NOT_FOUND));

        notif.setIntentosEnvio(notif.getIntentosEnvio() + 1);

        var resultado = notificacionSender.enviar(notif.getCanal(), notif.getDestino(), notif.getAsunto(), notif.getMensaje());

        notif.setEstadoEnvio(resultado.exitoso() ? EstadoEnvio.ENVIADO : EstadoEnvio.FALLIDO);
        notif.setDetalleResultado(resultado.detalleRespuesta());
        notif.setIdTransaccionExterno(resultado.transaccionId());
        notif.setFechaEnvio(OffsetDateTime.now());

        Notificacion guardada = notificacionRepository.save(notif);

        return new NotificacionEnvioResponseDto(
                guardada.getId(),
                guardada.getEstadoEnvio(),
                resultado.exitoso(),
                resultado.detalleRespuesta(),
                resultado.transaccionId(),
                guardada.getFechaEnvio()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDto> listarPorIncidente(UUID incidenteId) {
        return notificacionRepository.findByIncidenteIdOrderByCreatedAtDesc(incidenteId)
                .stream()
                .map(NotificacionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean apoderadosPrincipalesNotificados(UUID incidenteId) {
        return notificacionRepository.countNotificacionesEfectivasApoderadosPrincipales(incidenteId) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDto> listarPorApoderado(Long apoderadoId) {
        return notificacionRepository.findByApoderadoIdOrderByCreatedAtDesc(apoderadoId)
                .stream()
                .map(NotificacionMapper::toDto)
                .toList();
    }
}