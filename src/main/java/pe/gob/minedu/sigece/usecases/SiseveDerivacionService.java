package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.service.dto.SiseveDerivacionRequestDTO;
import pe.edu.cibertec.sigece.service.dto.SiseveDerivacionResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Encapsula la interoperabilidad/derivación hacia el sistema nacional
 * SíSeVe (MINEDU), exigida por la Ley N.° 29719 y el D.S. N.° 004-2018-MINEDU
 * para los casos que superan el ámbito de atención interna de la IE
 * (gravedad GRAVE/MUY_GRAVE, violencia sexual, reincidencia crítica, etc.).
 */
public interface SiseveDerivacionService {

    /**
     * Evalúa si un incidente cumple los criterios normativos de derivación
     * obligatoria a SíSeVe (p. ej. categoría SEXUAL, gravedad MUY_GRAVE).
     */
    boolean requiereDerivacionObligatoria(UUID incidenteId);

    /**
     * Deriva formalmente el caso a la plataforma SíSeVe, transiciona el
     * incidente a estado DERIVADO_SISEVE y persiste el código de referencia
     * devuelto por la plataforma nacional.
     */
    SiseveDerivacionResponseDTO derivarCaso(UUID incidenteId, SiseveDerivacionRequestDTO dto, Long usuarioId);

    /**
     * Consulta el estado actual de un caso ya derivado, contrastando la
     * fecha_reporte_siseve y el codigo_siseve almacenados localmente.
     */
    SiseveDerivacionResponseDTO consultarEstadoDerivacion(UUID incidenteId);

    /**
     * Lista todos los incidentes pendientes de derivación obligatoria
     * que aún no han sido reportados a SíSeVe (control de cumplimiento
     * normativo para el Director/Comité de Bienestar).
     */
    List<UUID> listarPendientesDeDerivacionObligatoria();
}