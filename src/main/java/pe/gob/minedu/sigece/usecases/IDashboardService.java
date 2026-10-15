package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardResponseDto;

/**
 * Servicio encargado de consolidar los indicadores analíticos de convivencia escolar y violencia reportada.
 * Caso de uso asociado: CU19.
 */
public interface IDashboardService {

    /**
     * Calcula y consolida las métricas e indicadores de convivencia escolar (tasa de resolución, reincidencias, tipologías recurrentes).
     * <p>Caso de uso: <b>CU19 · Dashboard de indicadores</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param filtro Parámetros de rango de fechas, niveles educativos y grados a analizar.
     * @return Consolidado analítico con gráficos y KPIs para el equipo directivo.
     */
    DashboardResponseDto obtenerIndicadores(DashboardFiltroRequestDto filtro);
}