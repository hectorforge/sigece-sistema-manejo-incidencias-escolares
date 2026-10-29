package pe.gob.minedu.sigece.dtos.tiposincidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.enums.Gravedad;

public record TipoIncidenciaRequestDto(
        @NotBlank(message = "El nombre del tipo de incidencia es obligatorio")
        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
        String nombre,

        @NotNull(message = "La categoría SíSeVe es obligatoria")
        CategoriaSiseve categoriaSiseve,

        @NotNull(message = "La gravedad es obligatoria")
        Gravedad gravedadBase,

        @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
        String descripcion
) {
}