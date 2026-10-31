package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaRequestDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaResponseDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoInicidenciaFiltroRequestDto;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;

import java.util.List;

/**
 * Servicio encargado del catálogo de tipologías de violencia e incidencias homologadas con SíSeVe.
 * Casos de uso asociados: CU06, CU07.
 */
public interface ITipoIncidenciaService {

    /**
     * Crea un nuevo tipo de incidencia en el catálogo institucional.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param dto Datos del tipo de incidencia a registrar.
     * @return Tipo de incidencia creado.
     */
    TipoIncidenciaResponseDto crear(TipoIncidenciaRequestDto dto);

    /**
     * Actualiza la información o categorización de un tipo de incidencia.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param id  Identificador único del tipo de incidencia.
     * @param dto Datos modificados.
     * @return Tipo de incidencia actualizado.
     */
    TipoIncidenciaResponseDto actualizar(Long id, TipoIncidenciaRequestDto dto);

    /**
     * Desactiva un tipo de incidencia para que no figure en nuevos registros.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param id Identificador del tipo de incidencia a desactivar.
     */
    void desactivar(Long id);

    /**
     * Reactiva un tipo de incidencia desactivado previamente.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param id Identificador del tipo de incidencia a reactivar.
     */
    void reactivar(Long id);

    /**
     * Obtiene el detalle de un tipo de incidencia por ID.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @param id Identificador del tipo de incidencia.
     * @return Información del tipo de incidencia.
     */
    TipoIncidenciaResponseDto obtenerPorId(Long id);

    /**
     * Lista todos los tipos de incidencia (activos e inactivos).
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia</b></p>
     * <p>Rol: Administrador</p>
     *
     * @return Lista completa del catálogo.
     */
    List<TipoIncidenciaResponseDto> listarTodos();

    /**
     * Busca y pagina tipos de incidencias mediante filtros (categoria siseve, gravedad).
     * <p>Rol: Administrador</p>
     *
     * @param filtro Criterios de búsqueda y paginación.
     * @return Página de tipos de incidentes coincidentes.
     */
    PagedResponse<TipoIncidenciaResponseDto> buscar(TipoInicidenciaFiltroRequestDto filtro);

    /**
     * Lista únicamente los tipos de incidencia actualmente activos.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Administrador, Personal educativo</p>
     *
     * @return Lista de tipos de incidencias activos.
     */
    List<TipoIncidenciaResponseDto> listarActivos();

    /**
     * Lista los tipos de incidencia asociados a una categoría específica de SíSeVe.
     * <p>Caso de uso: <b>CU06 · Gestionar tipos de incidencia, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Administrador, Personal educativo</p>
     *
     * @param categoria Categoría oficial de violencia escolar según SíSeVe.
     * @return Lista de tipos coincidentes con la categoría.
     */
    List<TipoIncidenciaResponseDto> listarPorCategoriaSiseve(CategoriaSiseve categoria);
}