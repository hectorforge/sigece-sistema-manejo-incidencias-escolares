package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionRequestDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveEvaluacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SisevePendienteResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado del cumplimiento normativo de derivación de casos graves al portal SíSeVe (MINEDU).
 * Casos de uso asociados: CU17, CU18, CU19.
 */
public interface ISiseveDerivacionService {

    /**
     * Determina si por la tipología y gravedad del caso existe obligación legal de derivarlo a SíSeVe.
     * <p>Caso de uso: <b>CU17 · Derivar a SíSeVe, CU18 · Generar alertas</b></p>
     * <p>Rol: Psicólogo, Personal de gestión, Scheduler</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @return {@code true} si es imperativa la derivación; {@code false} si es opcional o de manejo interno.
     */
    boolean requiereDerivacionObligatoria(UUID incidenteId);

    /**
     * Evalúa los antecedentes y la gravedad del caso emitiendo una recomendación formal técnica para derivación.
     * <p>Caso de uso: <b>CU17 · Derivar a SíSeVe</b></p>
     * <p>Rol: Psicólogo, Personal de gestión</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @return Evaluación y recomendación de derivación externa.
     */
    SiseveEvaluacionResponseDto evaluarDerivacion(UUID incidenteId);

    /**
     * Registra formalmente la derivación del caso hacia la plataforma externa de SíSeVe o el ingreso del código de registro oficial.
     * <p>Caso de uso: <b>CU17 · Derivar a SíSeVe</b></p>
     * <p>Rol: Psicólogo, Personal de gestión</p>
     *
     * @param incidenteId Identificador UUID del expediente.
     * @param dto         Código de reporte en SíSeVe, informe adjunto y observaciones de la derivación.
     * @param usuarioId   ID del usuario que realiza la derivación.
     * @return Constancia y detalles de la derivación efectuada.
     */
    SiseveDerivacionResponseDto derivarCaso(UUID incidenteId, SiseveDerivacionRequestDto dto, Long usuarioId);

    /**
     * Consulta el estado actual de la derivación ante el sistema SíSeVe para el caso dado.
     * <p>Caso de uso: <b>CU17 · Derivar a SíSeVe</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Estado del trámite de derivación.
     */
    SiseveDerivacionResponseDto consultarEstadoDerivacion(UUID incidenteId);

    /**
     * Lista todos los casos que requieren derivación obligatoria y que aún no han sido derivados.
     * <p>Caso de uso: <b>CU17 · Derivar a SíSeVe, CU18 · Generar alertas, CU19 · Dashboard de indicadores</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @return Lista de casos pendientes de derivación mandatoria.
     */
    List<SisevePendienteResponseDto> listarPendientesDeDerivacionObligatoria();
}