package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaRequestDto;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;

import java.util.List;

/**
 * Servicio transversal responsable del registro inmutable de auditoría para operaciones sensibles del sistema.
 * Casos de uso asociados: CU02, CU03, CU07, CU09, CU10, CU11, CU13, CU14, CU15, CU16, CU17, CU20.
 */
public interface IAuditoriaService {

    /**
     * Registra una traza inmutable del evento sensible ejecutado en la tabla de auditoría.
     * <p>Casos de uso: Transversal (CU07, CU10, CU11, CU13, CU14, CU15, CU16, CU17, CU20)</p>
     *
     * @param dto Información del evento, usuario autor, entidad afectada, IP y detalle de los cambios.
     */
    void registrar(AuditoriaRequestDto dto);

    /**
     * Lista la trazabilidad histórica de eventos asociados a una entidad específica (ej. entidad 'INCIDENTE' con su ID).
     * <p>Caso de uso: <b>CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado, Auditor</p>
     *
     * @param entidad   Nombre de la entidad registrada (ej. "INCIDENTE", "USUARIO").
     * @param entidadId Identificador como texto de la entidad consultada.
     * @return Lista cronológica de eventos de auditoría registrados.
     */
    List<AuditoriaResponseDto> listarPorEntidad(String entidad, String entidadId);

    /**
     * Realiza búsquedas avanzadas y paginadas en la bitácora general de auditoría institucional.
     * <p>Caso de uso: <b>CU09 · Consultar incidencia</b></p>
     * <p>Rol: Administrador, Auditor</p>
     *
     * @param filtro Criterios de búsqueda (tipo de evento, usuario, rango de fechas).
     * @return Página de registros de auditoría.
     */
    PagedResponse<AuditoriaResponseDto> buscar(AuditoriaFiltroRequestDto filtro);
}