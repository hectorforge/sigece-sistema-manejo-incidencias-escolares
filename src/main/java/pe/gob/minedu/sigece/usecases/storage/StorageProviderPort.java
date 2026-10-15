package pe.gob.minedu.sigece.usecases.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageProviderPort implements IStorageProviderPort{
    @Override
    public String subirArchivo(String nombreAlmacenamiento, MultipartFile archivo) {
        return "";
    }

    @Override
    public String generarUrlTemporalLectura(String nombreAlmacenamiento, int minutosExpiracion) {
        return "";
    }

    @Override
    public byte[] descargarArchivo(String nombreAlmacenamiento) {
        return new byte[0];
    }

    @Override
    public void eliminarArchivo(String nombreAlmacenamiento) {

    }
}
