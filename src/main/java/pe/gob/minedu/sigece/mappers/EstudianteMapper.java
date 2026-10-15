package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.models.Estudiante;

public final class EstudianteMapper {
    private EstudianteMapper() {}

    public static Estudiante toEntity(EstudianteRequestDto dto) {
        Estudiante entity = new Estudiante();
        entity.setCodigoSiagie(dto.codigoSiagie().trim());
        entity.setNombres(dto.nombres().trim());
        entity.setApellidos(dto.apellidos().trim());
        entity.setFechaNacimiento(dto.fechaNacimiento());
        entity.setNivel(dto.nivel());
        entity.setGrado(dto.grado().trim().toUpperCase());
        entity.setSeccion(dto.seccion().trim().toUpperCase());
        entity.setActivo(true);
        return entity;
    }

    public static EstudianteResponseDto toDto(Estudiante entity) {
        return new EstudianteResponseDto(
                entity.getId(),
                entity.getCodigoSiagie(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getFechaNacimiento(),
                entity.getNivel(),
                entity.getGrado(),
                entity.getSeccion(),
                entity.getActivo(),
                entity.getCreatedAt()
        );
    }
}