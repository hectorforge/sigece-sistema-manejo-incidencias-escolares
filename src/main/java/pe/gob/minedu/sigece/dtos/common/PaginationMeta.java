package pe.gob.minedu.sigece.dtos.common;

/**
 * Metadatos de una consulta paginada.
 *
 * @param page          Número de la página actual, comenzando desde cero.
 * @param size          Tamaño de la página.
 * @param totalElements Cantidad total de elementos encontrados.
 * @param totalPages    Cantidad total de páginas.
 */
public record PaginationMeta(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}