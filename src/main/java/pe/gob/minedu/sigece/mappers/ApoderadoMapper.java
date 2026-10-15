package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.models.Apoderado;
import pe.gob.minedu.sigece.models.EstudianteApoderado;

public final class ApoderadoMapper {
    private ApoderadoMapper() {}

    public static Apoderado toEntity(ApoderadoRequestDto dto) {
        Apoderado entity = new Apoderado();
        entity.setNombres(dto.nombres().trim());
        entity.setApellidos(dto.apellidos().trim());
        entity.setTipoDocumento(dto.tipoDocumento());
        entity.setNumeroDocumento(dto.numeroDocumento().trim());
        entity.setTelefono(dto.telefono().trim());
        entity.setCorreo(dto.correo() != null ? dto.correo().trim().toLowerCase() : null);
        return entity;
    }

    public static ApoderadoResponseDto toDto(Apoderado entity) {
        return new ApoderadoResponseDto(
                entity.getId(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getTipoDocumento(),
                entity.getNumeroDocumento(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getCreatedAt()
        );
    }

    public static VinculoFamiliarResponseDto toVinculoDto(EstudianteApoderado ea) {
        return new VinculoFamiliarResponseDto(
                ea.getEstudiante().getId(),
                ea.getApoderado().getId(),
                ea.getApoderado().getNombres() + " " + ea.getApoderado().getApellidos(),
                ea.getEstudiante().getNombres() + " " + ea.getEstudiante().getApellidos(),
                ea.getApoderado().getNumeroDocumento(),
                ea.getParentesco(),
                ea.getEsContactoPrincipal(),
                ea.getApoderado().getTelefono(),
                ea.getApoderado().getCorreo()
        );
    }
}