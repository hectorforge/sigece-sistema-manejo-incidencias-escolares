package pe.gob.minedu.sigece.usecases.storage;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.storage.StorageResult;

public interface IStorageProviderPort {

    StorageResult subirArchivo(String nombreAlmacenamiento, MultipartFile archivo);
    String generarUrlTemporalLectura(String nombreAlmacenamiento, int minutosExpiracion);
    byte[] descargarArchivo(String nombreAlmacenamiento);
    void eliminarArchivo(String nombreAlmacenamiento);
}