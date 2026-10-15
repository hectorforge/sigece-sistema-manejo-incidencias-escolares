package pe.gob.minedu.sigece.dtos.reportes;

public record ArchivoResponseDto(
        String nombreArchivo,
        String tipoMime,
        byte[] contenido,
        long tamanoBytes
) {
}