package pe.gob.minedu.sigece.usecases.storage;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageProviderPort {
    String subirArchivo(String nombreAlmacenamiento, MultipartFile archivo);
    String generarUrlTemporalLectura(String nombreAlmacenamiento, int minutosExpiracion);
    byte[] descargarArchivo(String nombreAlmacenamiento);
    void eliminarArchivo(String nombreAlmacenamiento);
}