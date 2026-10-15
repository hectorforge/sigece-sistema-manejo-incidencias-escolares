package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionRequestDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveEvaluacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SisevePendienteResponseDto;
import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.ISiseveDerivacionService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiseveDerivacionServiceImpl implements ISiseveDerivacionService {

    private final IIncidenteRepository incidenteRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean requiereDerivacionObligatoria(UUID incidenteId) {
        return incidenteRepository.findById(incidenteId)
                .map(i -> i.getGravedad() == Gravedad.GRAVE || i.getGravedad() == Gravedad.MUY_GRAVE)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public SiseveEvaluacionResponseDto evaluarDerivacion(UUID incidenteId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        boolean obligatoria = incidente.getGravedad() == Gravedad.GRAVE || incidente.getGravedad() == Gravedad.MUY_GRAVE;

        return new SiseveEvaluacionResponseDto(
                incidente.getId(),
                incidente.getCodigoCaso(),
                obligatoria,
                "D.S. N.° 004-2018-MINEDU / Lineamientos para la Gestión de la Convivencia Escolar",
                obligatoria
                        ? "El caso califica como grave/muy grave. Requiere reporte y elevación a la plataforma SíSeVe dentro de las 24 horas."
                        : "El caso puede ser abordado internamente por la IE conforme al protocolo de medidas correctivas."
        );
    }

    @Override
    @Transactional
    public SiseveDerivacionResponseDto derivarCaso(UUID incidenteId, SiseveDerivacionRequestDto dto, Long usuarioId) {
        log.info("Registrando derivación a SíSeVe del caso ID: {} por usuario ID: {}", incidenteId, usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        if (incidente.getEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_CERRADO);
        }

        incidente.setReportadoSiseve(true);
        incidente.setEstadoDerivacion(EstadoDerivacion.REGISTRADA);
        incidente.setModoDerivacion(dto.modoDerivacion());
        incidente.setCodigoSiseve(dto.codigoSiseve().trim());
        incidente.setFechaReporteSiseve(OffsetDateTime.now());
        incidente.setObservacionesSiseve(dto.observacionesSiseve());
        incidente.setDerivadoPor(usuario);
        incidente.setEstado(EstadoIncidente.DERIVADO_SISEVE);

        Incidente guardado = incidenteRepository.save(incidente);

        return new SiseveDerivacionResponseDto(
                guardado.getId(),
                guardado.getCodigoCaso(),
                guardado.getReportadoSiseve(),
                guardado.getEstadoDerivacion(),
                guardado.getModoDerivacion(),
                guardado.getCodigoSiseve(),
                guardado.getFechaReporteSiseve(),
                guardado.getObservacionesSiseve(),
                usuario.getNombres() + " " + usuario.getApellidos()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SiseveDerivacionResponseDto consultarEstadoDerivacion(UUID incidenteId) {
        return incidenteRepository.findById(incidenteId)
                .map(i -> new SiseveDerivacionResponseDto(
                        i.getId(),
                        i.getCodigoCaso(),
                        i.getReportadoSiseve(),
                        i.getEstadoDerivacion(),
                        i.getModoDerivacion(),
                        i.getCodigoSiseve(),
                        i.getFechaReporteSiseve(),
                        i.getObservacionesSiseve(),
                        i.getDerivadoPor() != null ? i.getDerivadoPor().getNombres() + " " + i.getDerivadoPor().getApellidos() : null
                ))
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SisevePendienteResponseDto> listarPendientesDeDerivacionObligatoria() {
        return incidenteRepository.findPendientesDeDerivacionSiseve().stream()
                .map(i -> new SisevePendienteResponseDto(
                        i.getId(),
                        i.getCodigoCaso(),
                        i.getTipoIncidencia().getNombre(),
                        i.getGravedad(),
                        i.getFechaOcurrencia(),
                        java.time.Duration.between(i.getFechaOcurrencia(), OffsetDateTime.now()).toDays()
                ))
                .toList();
    }
}