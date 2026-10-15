package pe.gob.minedu.sigece.dtos.adjuntos;

public record IntegridadResponseDto(
        Long adjuntoId,
        String hashRegistrado,
        String hashCalculado,
        boolean integro,
        String detalle
) {
}