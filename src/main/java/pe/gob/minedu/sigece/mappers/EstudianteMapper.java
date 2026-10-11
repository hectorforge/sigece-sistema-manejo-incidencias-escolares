package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.models.Estudiante;

public class EstudianteMapper {

    private EstudianteMapper() {

    }

    public static Estudiante toEntity(EstudianteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setCodigoSiagie(dto.codigoSiagie());
        estudiante.setNombres(dto.nombres());
        estudiante.setApellidos(dto.apellidos());
        estudiante.setFechaNacimiento(dto.fechaNacimiento());
        estudiante.setNivel(dto.nivel());
        estudiante.setGrado(dto.grado());
        estudiante.setSeccion(dto.seccion());
        estudiante.setActivo(true);
        return estudiante;
    }

    public static EstudianteResponseDto toDto(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }

        return new EstudianteResponseDto(
                estudiante.getId(),
                estudiante.getCodigoSiagie(),
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getFechaNacimiento(),
                estudiante.getNivel(),
                estudiante.getGrado(),
                estudiante.getSeccion(),
                estudiante.getActivo(),
                estudiante.getCreatedAt(),
                estudiante.getUpdatedAt()
        );
    }
}