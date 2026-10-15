package pe.gob.minedu.sigece.dtos.usuarios;

public record UsuarioFiltroRequestDto(
        String termino,
        Long roleId,
        Boolean activo,
        Integer page,
        Integer size
) {
}