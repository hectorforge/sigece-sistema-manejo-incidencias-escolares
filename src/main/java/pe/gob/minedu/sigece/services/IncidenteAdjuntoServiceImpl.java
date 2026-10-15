package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.adjuntos.*;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.AdjuntoMapper;
import pe.gob.minedu.sigece.models.AccionSeguimiento;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.IncidenteAdjunto;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IAccionSeguimientoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteAdjuntoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.IIncidenteAdjuntoService;
import pe.gob.minedu.sigece.usecases.storage.IStorageProviderPort;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidenteAdjuntoServiceImpl implements IIncidenteAdjuntoService {

    private final IIncidenteAdjuntoRepository adjuntoRepository;
    private final IIncidenteRepository incidenteRepository;
    private final IAccionSeguimientoRepository accionRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IStorageProviderPort storageProvider;

    @Override
    @Transactional
    public AdjuntoResponseDto subirEvidencia(UUID incidenteId, AdjuntoRequestDto dto, MultipartFile archivo, Long usuarioId) {
        if (archivo == null || archivo.isEmpty()) {
            throw new OperacionInvalidaException(ErrorCatalog.BAD_REQUEST, "El archivo enviado está vacío");
        }
        if (archivo.getSize() > 10485760) {
            throw new OperacionInvalidaException(ErrorCatalog.ADJUNTO_TAMANO_EXCEDIDO);
        }

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        AccionSeguimiento accion = dto.accionId() != null
                ? accionRepository.findById(dto.accionId()).orElse(null)
                : null;

        String hashCalculado = calcularSha256(archivo);

        if (adjuntoRepository.existsByIncidenteIdAndHashSha256AndActivoTrue(incidenteId, hashCalculado)) {
            throw new RecursoDuplicadoException(ErrorCatalog.BAD_REQUEST, "Esta evidencia ya fue adjuntada al expediente");
        }

        String nombreAlmacenamiento = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        String urlStorage = storageProvider.subirArchivo(nombreAlmacenamiento, archivo);

        IncidenteAdjunto adjunto = new IncidenteAdjunto();
        adjunto.setIncidente(incidente);
        adjunto.setAccion(accion);
        adjunto.setSubidoPor(usuario);
        adjunto.setNombreOriginal(archivo.getOriginalFilename());
        adjunto.setNombreAlmacenamiento(nombreAlmacenamiento);
        adjunto.setDescripcion(dto.descripcion().trim());
        adjunto.setTipoMime(archivo.getContentType() != null ? archivo.getContentType() : "application/octet-stream");
        adjunto.setTamanoBytes(archivo.getSize());
        adjunto.setUrlStorage(urlStorage);
        adjunto.setHashSha256(hashCalculado);
        adjunto.setActivo(true);

        return AdjuntoMapper.toDto(adjuntoRepository.save(adjunto));
    }

    @Override
    @Transactional
    public List<AdjuntoResponseDto> subirEvidencias(UUID incidenteId, AdjuntoRequestDto dto, List<MultipartFile> archivos, Long usuarioId) {
        List<AdjuntoResponseDto> lista = new ArrayList<>();
        for (MultipartFile f : archivos) {
            lista.add(subirEvidencia(incidenteId, dto, f, usuarioId));
        }
        return lista;
    }

    @Override
    @Transactional(readOnly = true)
    public AdjuntoUrlResponseDto generarUrlDescargaTemporal(Long adjuntoId, int minutosExpiracion) {
        IncidenteAdjunto adjunto = adjuntoRepository.findById(adjuntoId)
                .filter(IncidenteAdjunto::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ADJUNTO_NOT_FOUND));

        String url = storageProvider.generarUrlTemporalLectura(adjunto.getNombreAlmacenamiento(), minutosExpiracion);

        return new AdjuntoUrlResponseDto(
                adjunto.getId(),
                adjunto.getNombreOriginal(),
                url,
                OffsetDateTime.now().plusMinutes(minutosExpiracion)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public IntegridadResponseDto verificarIntegridad(Long adjuntoId) {
        IncidenteAdjunto adjunto = adjuntoRepository.findById(adjuntoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ADJUNTO_NOT_FOUND));

        byte[] bytes = storageProvider.descargarArchivo(adjunto.getNombreAlmacenamiento());
        String hashReal = calcularSha256Bytes(bytes);
        boolean integro = hashReal.equalsIgnoreCase(adjunto.getHashSha256());

        return new IntegridadResponseDto(
                adjunto.getId(),
                adjunto.getHashSha256(),
                hashReal,
                integro,
                integro ? "Cadena de custodia íntegra" : "La firma criptográfica difiere del archivo almacenado"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdjuntoResponseDto> listarPorIncidente(UUID incidenteId) {
        return adjuntoRepository.findByIncidenteIdAndActivoTrueOrderByCreatedAtDesc(incidenteId)
                .stream()
                .map(AdjuntoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarAdjunto(Long adjuntoId, EliminarAdjuntoRequestDto dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        IncidenteAdjunto adjunto = adjuntoRepository.findById(adjuntoId)
                .filter(IncidenteAdjunto::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ADJUNTO_NOT_FOUND));

        adjunto.setActivo(false);
        adjunto.setFechaEliminacion(OffsetDateTime.now());
        adjunto.setEliminadoPor(usuario);
        adjunto.setMotivoEliminacion(dto.motivoEliminacion().trim());

        adjuntoRepository.save(adjunto);
    }

    private String calcularSha256(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (DigestInputStream dis = new DigestInputStream(is, md)) {
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1) {}
            }
            return HexFormat.of().formatHex(md.digest());
        } catch (Exception e) {
            throw new OperacionInvalidaException(ErrorCatalog.ADJUNTO_ALMACENAMIENTO_ERROR, "No se pudo calcular el hash de la evidencia");
        }
    }

    private String calcularSha256Bytes(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(bytes));
        } catch (Exception e) {
            throw new OperacionInvalidaException(ErrorCatalog.ADJUNTO_ALMACENAMIENTO_ERROR, "Error comprobando el hash");
        }
    }
}