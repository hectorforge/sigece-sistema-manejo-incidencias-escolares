package pe.gob.minedu.sigece.dtos.involucrados;

import pe.gob.minedu.sigece.enums.RolInvolucrado;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InvolucradoResponseDto(
        Long id,
        UUID incidenteId,
        Long estudianteId,
        Long usuarioId,
        String nombreCompleto,
        String documentoIdentidad,
        String tipoInvolucrado, // ESTUDIANTE o PERSONAL
        RolInvolucrado rolInvolucrado,
        String observaciones,
        OffsetDateTime createdAt
) {
}