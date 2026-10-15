package pe.gob.minedu.sigece.dtos.common;

import java.util.List;

/**
 * Representa el resultado de una consulta paginada.
 *
 * @param <T> Tipo de los elementos que contiene la página.
 * @param content Elementos de la página actual.
 * @param meta Metadatos de paginación.
 */
public record PagedResponse<T>(
        List<T> content,
        PaginationMeta meta
) {
}