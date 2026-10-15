package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.reportes.ArchivoResponseDto;
import pe.gob.minedu.sigece.dtos.reportes.ExportarReporteRequestDto;
import pe.gob.minedu.sigece.dtos.reportes.ReporteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.reportes.ReporteResponseDto;
import pe.gob.minedu.sigece.dtos.reportes.TipoReporteResponseDto;

import java.util.List;

/**
 * Servicio encargado de la formulación, generación y exportación segura de reportes ejecutivos e institucionales.
 * Caso de uso asociado: CU20.
 */
public interface IReporteService {

    /**
     * Lista los tipos de reportes preconfigurados disponibles en el sistema (Libro de Registro, Balance Bimestral, Derivaciones, etc.).
     * <p>Caso de uso: <b>CU20 · Generar y exportar reportes</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @return Catálogo de reportes disponibles.
     */
    List<TipoReporteResponseDto> listarTiposReporte();

    /**
     * Procesa y genera los datos consolidados de un reporte para su visualización preliminar.
     * <p>Caso de uso: <b>CU20 · Generar y exportar reportes</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param filtro    Filtros del reporte (fechas, tipo de reporte, grados).
     * @param usuarioId ID del usuario directivo solicitante.
     * @return Vista previa estructurada del reporte solicitado.
     */
    ReporteResponseDto generar(ReporteFiltroRequestDto filtro, Long usuarioId);

    /**
     * Genera y exporta el archivo físico del reporte (formato PDF o Excel) aplicando anonimización de datos de menores si corresponde.
     * <p>Caso de uso: <b>CU20 · Generar y exportar reportes</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param dto       Formato de salida requerido, campos a exportar y filtros aplicados.
     * @param usuarioId ID del usuario que descarga el documento.
     * @return Archivo binario con nombre, tipo MIME y contenido generado.
     */
    ArchivoResponseDto exportar(ExportarReporteRequestDto dto, Long usuarioId);
}