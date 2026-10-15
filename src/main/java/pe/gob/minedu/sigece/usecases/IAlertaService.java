package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.alertas.AlertaFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.alertas.AlertaResponseDto;
import pe.gob.minedu.sigece.dtos.alertas.AtenderAlertaRequestDto;
import pe.gob.minedu.sigece.dtos.alertas.ChequeoAlertasResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;

/**
 * Servicio encargado del motor de alertas tempranas por inacción, vencimientos o reincidencias de casos.
 * Caso de uso asociado: CU18.
 */
public interface IAlertaService {

    /**
     * Ejecuta la rutina automática programada que identifica plazos vencidos, reincidencias y genera las alertas del sistema.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Scheduler institucional / Job de sistema</p>
     *
     * @return Resumen estadístico del proceso de verificación ejecutado.
     */
    ChequeoAlertasResponseDto ejecutarChequeoDiario();

    /**
     * Lista y filtra de forma paginada las alertas activas o atendidas de la institución.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Personal de gestión (consulta)</p>
     *
     * @param filtro    Filtros por criticidad, tipo de alerta, fechas o estado.
     * @param usuarioId ID del usuario que consulta las alertas.
     * @return Página de alertas institucionales.
     */
    PagedResponse<AlertaResponseDto> listarAlertas(AlertaFiltroRequestDto filtro, Long usuarioId);

    /**
     * Cuenta el total de alertas activas y no resueltas que requieren atención inmediata por el usuario o su área.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Personal de gestión (consulta)</p>
     *
     * @param usuarioId Identificador del usuario consultante.
     * @return Cantidad de alertas activas pendientes.
     */
    int contarActivas(Long usuarioId);

    /**
     * Marca una alerta como atendida registrando la acción correctiva ejecutada.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param alertaId  Identificador único de la alerta.
     * @param dto       Descripción del descargo o acción realizada para levantar la alerta.
     * @param usuarioId ID del usuario que atiende la alerta.
     * @return Alerta actualizada a estado atendido.
     */
    AlertaResponseDto marcarAtendida(Long alertaId, AtenderAlertaRequestDto dto, Long usuarioId);
}