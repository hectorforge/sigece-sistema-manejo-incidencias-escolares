package pe.gob.minedu.sigece.dtos.common;

import java.util.List;

/**
 * Representa el resultado de una consulta paginada.
 *
 * @param <T> Tipo de los elementos que contiene la página.
 */
public record PagedResponse<T>(
        List<T> content,
        ApiResponse.PaginationMeta meta
) {
}
