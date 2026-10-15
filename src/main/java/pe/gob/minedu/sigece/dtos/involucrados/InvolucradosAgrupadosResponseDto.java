package pe.gob.minedu.sigece.dtos.involucrados;

import java.util.List;

public record InvolucradosAgrupadosResponseDto(
        List<InvolucradoResponseDto> victimas,
        List<InvolucradoResponseDto> agresores,
        List<InvolucradoResponseDto> testigos
) {
}