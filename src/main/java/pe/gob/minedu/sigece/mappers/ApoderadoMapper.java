package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.models.Apoderado;

public class ApoderadoMapper {

    private ApoderadoMapper() {

    }

    public static Apoderado toEntity(ApoderadoRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Apoderado apoderado = new Apoderado();
        apoderado.setNombres(dto.nombres());
        apoderado.setApellidos(dto.apellidos());
        apoderado.setTipoDocumento(dto.tipoDocumento());
        apoderado.setNumeroDocumento(dto.numeroDocumento());
        apoderado.setTelefono(dto.telefono());
        apoderado.setCorreo(dto.correo());
        return apoderado;
    }

    public static ApoderadoResponseDto toDto(Apoderado apoderado) {
        if (apoderado == null) {
            return null;
        }

        return new ApoderadoResponseDto(
                apoderado.getId(),
                apoderado.getNombres(),
                apoderado.getApellidos(),
                apoderado.getTipoDocumento(),
                apoderado.getNumeroDocumento(),
                apoderado.getTelefono(),
                apoderado.getCorreo(),
                apoderado.getCreatedAt(),
                apoderado.getUpdatedAt()
        );
    }
}