package pe.gob.minedu.sigece.usecases;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.adjuntos.AdjuntoRequestDto;
import pe.gob.minedu.sigece.dtos.adjuntos.AdjuntoResponseDto;
import pe.gob.minedu.sigece.dtos.adjuntos.AdjuntoUrlResponseDto;
import pe.gob.minedu.sigece.dtos.adjuntos.EliminarAdjuntoRequestDto;
import pe.gob.minedu.sigece.dtos.adjuntos.IntegridadResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de la gestión de evidencias digitales (Azure Blob Storage) con hash SHA-256.
 * Casos de uso asociados: CU07, CU09, CU15.
 */
public interface IIncidenteAdjuntoService {

    /**
     * Sube y asocia una evidencia digital individual al expediente calculando su hash de integridad.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Tutor, Psicólogo</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @param dto         Metadatos de la evidencia (tipo, descripción).
     * @param archivo     Archivo binario recibido vía multipart.
     * @param usuarioId   ID del usuario que sube el archivo.
     * @return Información de la evidencia almacenada.
     */
    AdjuntoResponseDto subirEvidencia(UUID incidenteId, AdjuntoRequestDto dto, MultipartFile archivo, Long usuarioId);

    /**
     * Sube múltiples evidencias digitales en un único proceso por lotes.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Tutor, Psicólogo</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @param dto         Metadatos comunes o descriptivos de los adjuntos.
     * @param archivos    Colección de archivos binarios recibidos.
     * @param usuarioId   ID del usuario que sube los archivos.
     * @return Lista de evidencias almacenadas.
     */
    List<AdjuntoResponseDto> subirEvidencias(UUID incidenteId, AdjuntoRequestDto dto, List<MultipartFile> archivos, Long usuarioId);

    /**
     * Genera un URL temporal y seguro (SAS Token) para la visualización o descarga de un archivo.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param adjuntoId         Identificador único del adjunto.
     * @param minutosExpiracion Tiempo de vida en minutos del URL temporal generado.
     * @return Enlace temporal firmado de descarga.
     */
    AdjuntoUrlResponseDto generarUrlDescargaTemporal(Long adjuntoId, int minutosExpiracion);

    /**
     * Verifica la integridad criptográfica del archivo comparando su contenido en Blob Storage contra el SHA-256 registrado.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param adjuntoId Identificador del archivo a comprobar.
     * @return Resultado del chequeo de integridad y correspondencia de hash.
     */
    IntegridadResponseDto verificarIntegridad(Long adjuntoId);

    /**
     * Lista todas las evidencias digitales asociadas a un caso.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @return Lista de adjuntos asociados al caso.
     */
    List<AdjuntoResponseDto> listarPorIncidente(UUID incidenteId);

    /**
     * Elimina lógicamente un archivo adjunto del expediente justificando el motivo en auditoría.
     * <p>Caso de uso: <b>CU15 · Gestionar evidencias</b></p>
     * <p>Rol: Tutor, Psicólogo</p>
     *
     * @param adjuntoId Identificador del archivo adjunto a eliminar.
     * @param dto       Justificación técnica o legal del retiro.
     * @param usuarioId ID del usuario que solicita la eliminación.
     */
    void eliminarAdjunto(Long adjuntoId, EliminarAdjuntoRequestDto dto, Long usuarioId);
}