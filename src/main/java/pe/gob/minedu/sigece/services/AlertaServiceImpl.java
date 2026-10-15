package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.alertas.AlertaFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.alertas.AlertaResponseDto;
import pe.gob.minedu.sigece.dtos.alertas.AtenderAlertaRequestDto;
import pe.gob.minedu.sigece.dtos.alertas.ChequeoAlertasResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.enums.EstadoAlerta;
import pe.gob.minedu.sigece.enums.OrigenAlerta;
import pe.gob.minedu.sigece.enums.TipoAlerta;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.AlertaMapper;
import pe.gob.minedu.sigece.models.Alerta;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IAccionSeguimientoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IAlertaRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.persistence.specs.AlertaSpecification;
import pe.gob.minedu.sigece.usecases.IAlertaService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements IAlertaService {

    private final IAlertaRepository alertaRepository;
    private final IIncidenteRepository incidenteRepository;
    private final IAccionSeguimientoRepository accionRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ChequeoAlertasResponseDto ejecutarChequeoDiario() {
        log.info("Iniciando chequeo automático diario de alertas tempranas");
        int demoras = 0;
        int vencidos = 0;

        // Demora de atención (+72h en REGISTRADO)
        var casosDemorados = incidenteRepository.findCasosDemorados(OffsetDateTime.now().minusHours(72));
        for (Incidente i : casosDemorados) {
            if (!alertaRepository.existsAlertaActiva(i.getId(), TipoAlerta.DEMORA_ATENCION, null)) {
                Alerta a = new Alerta();
                a.setIncidente(i);
                a.setTipoAlerta(TipoAlerta.DEMORA_ATENCION);
                a.setOrigen(OrigenAlerta.AUTOMATICA);
                a.setMensaje("El caso " + i.getCodigoCaso() + " supera el plazo de 72 horas sin ser asignado ni atendido.");
                a.setEstadoAlerta(EstadoAlerta.PENDIENTE);
                alertaRepository.save(a);
                demoras++;
            }
        }

        // Seguimientos vencidos
        var accionesVencidas = accionRepository.findAccionesVencidas(LocalDate.now());
        for (var acc : accionesVencidas) {
            if (!alertaRepository.existsAlertaActiva(acc.getIncidente().getId(), TipoAlerta.SEGUIMIENTO_VENCIDO, null)) {
                Alerta a = new Alerta();
                a.setIncidente(acc.getIncidente());
                a.setUsuarioDestino(acc.getUsuario());
                a.setTipoAlerta(TipoAlerta.SEGUIMIENTO_VENCIDO);
                a.setOrigen(OrigenAlerta.AUTOMATICA);
                a.setMensaje("La acción '" + acc.getTipoAccion() + "' del caso " + acc.getIncidente().getCodigoCaso() + " ha vencido.");
                a.setEstadoAlerta(EstadoAlerta.PENDIENTE);
                alertaRepository.save(a);
                vencidos++;
            }
        }

        return new ChequeoAlertasResponseDto(
                demoras + vencidos,
                demoras,
                0,
                vencidos,
                OffsetDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AlertaResponseDto> listarAlertas(AlertaFiltroRequestDto filtro, Long usuarioId) {
        int pageNumber = Optional.ofNullable(filtro).map(AlertaFiltroRequestDto::page).orElse(0);
        int pageSize = Optional.ofNullable(filtro).map(AlertaFiltroRequestDto::size).orElse(10);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = alertaRepository.findAll(AlertaSpecification.conFiltro(filtro, usuarioId), pageable);
        var content = page.map(AlertaMapper::toDto).getContent();
        var meta = new PaginationMeta(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());

        return new PagedResponse<>(content, meta);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarActivas(Long usuarioId) {
        return alertaRepository.countAlertasActivas(usuarioId);
    }

    @Override
    @Transactional
    public AlertaResponseDto marcarAtendida(Long alertaId, AtenderAlertaRequestDto dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.RESOURCE_NOT_FOUND));

        alerta.setEstadoAlerta(dto.nuevoEstado());
        alerta.setObservacionResolucion(dto.observacionResolucion().trim());
        alerta.setResueltaPor(usuario);
        alerta.setFechaResolucion(OffsetDateTime.now());

        return AlertaMapper.toDto(alertaRepository.save(alerta));
    }
}