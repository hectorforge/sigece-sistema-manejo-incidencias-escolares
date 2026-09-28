package pe.gob.minedu.sigece.usecases;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.cibertec.sigece.service.dto.AdjuntoDTO;

import java.util.List;
import java.util.UUID;

/**
 * Gestiona las evidencias digitales del caso (capturas de ciberacoso,
 * actas firmadas escaneadas, fotografías) sobre Azure Blob Storage,
 * resolviendo la pérdida probatoria propia del expediente físico.
 */
public interface IncidenteAdjuntoService {

    /**
     * Sube un archivo de evidencia al contenedor Azure Blob Storage,
     * calcula su hash SHA-256 (cadena de custodia) y persiste sus
     * metadatos asociados al incidente (y opcionalmente a una acción
     * de seguimiento específica).
     */
    AdjuntoDTO subirEvidencia(UUID incidenteId, Long accionId, MultipartFile archivo, Long usuarioId);

    /**
     * Genera una URL de descarga temporal (SAS token) para visualizar
     * o descargar el archivo de forma segura y auditable.
     */
    String generarUrlDescargaTemporal(Long adjuntoId, int minutosExpiracion);

    /**
     * Verifica la integridad del archivo comparando el hash SHA-256
     * almacenado contra el hash recalculado del blob, para descartar
     * alteración de evidencia probatoria.
     */
    boolean verificarIntegridad(Long adjuntoId);

    /**
     * Lista todas las evidencias digitales adjuntas a un incidente.
     */
    List<AdjuntoDTO> listarPorIncidente(UUID incidenteId);

    /**
     * Elimina un adjunto (soft-delete recomendado a nivel de blob;
     * restringido a roles ROLE_DIRECTOR / ROLE_ADMIN) dejando registro
     * de auditoría de quién y cuándo lo eliminó.
     */
    void eliminarAdjunto(Long adjuntoId, Long usuarioId);
}