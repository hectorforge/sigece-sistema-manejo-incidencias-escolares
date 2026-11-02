package pe.gob.minedu.sigece.usecases.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.storage.StorageResult;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CloudinaryStorageProviderPort implements IStorageProviderPort {

    private final Cloudinary cloudinary;

    public CloudinaryStorageProviderPort(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public StorageResult subirArchivo(String identificadorIncidencia, MultipartFile archivo) {
        try {
            String nombreOriginal = (archivo.getOriginalFilename() != null) ? archivo.getOriginalFilename() : "archivo";
            String extension = obtenerExtension(nombreOriginal).toLowerCase();

            // Clasificar carpeta exacta según extensión
            String carpetaDestino = determinarCarpeta(extension);
            String rutaCarpeta = "incidentes/" + carpetaDestino;

            // Identificador único: inc_{id}_{uuid}
            String sufijoUnico = UUID.randomUUID().toString().substring(0, 8);
            String publicId = "inc_" + identificadorIncidencia + "_" + sufijoUnico;

            // Para documentos (raw) es vital conservar la extensión en el public_id
            if (carpetaDestino.equals("documentos") && !extension.isEmpty()) {
                publicId += "." + extension;
            }

            // Parámetros de subida a Cloudinary
            Map<String, Object> params = ObjectUtils.asMap(
                    "folder", rutaCarpeta,
                    "public_id", publicId,
                    "resource_type", "auto",
                    "overwrite", true
            );

            Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), params);

            String publicIdCompleto = uploadResult.get("public_id").toString();
            String urlSegura = uploadResult.get("secure_url").toString();

            return new StorageResult(publicIdCompleto, urlSegura);

        } catch (Exception e) {
            log.error("Error al subir archivo a Cloudinary para la incidencia {}: {}", identificadorIncidencia, e.getMessage());
            throw new RuntimeException("Error al subir el archivo a Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public String generarUrlTemporalLectura(String publicIdConRuta, int minutosExpiracion) {
        try {
            String resourceType = determinarResourceType(publicIdConRuta);
            return cloudinary.url()
                    .resourceType(resourceType)
                    .secure(true)
                    .generate(publicIdConRuta);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la URL temporal de Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] descargarArchivo(String publicIdConRuta) {
        try {
            String resourceType = determinarResourceType(publicIdConRuta);
            String fileUrl = cloudinary.url()
                    .resourceType(resourceType)
                    .secure(true)
                    .generate(publicIdConRuta);

            try (InputStream in = new URL(fileUrl).openStream()) {
                return in.readAllBytes();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo desde Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarArchivo(String publicIdConRuta) {
        try {
            String resourceType = determinarResourceType(publicIdConRuta);
            Map result = cloudinary.uploader().destroy(publicIdConRuta, ObjectUtils.asMap("resource_type", resourceType));
            log.info("Archivo eliminado de Cloudinary [{}]: {}", publicIdConRuta, result.get("result"));
        } catch (Exception e) {
            log.error("Error al eliminar archivo en Cloudinary [{}]: {}", publicIdConRuta, e.getMessage());
            throw new RuntimeException("Error al eliminar el archivo de Cloudinary: " + e.getMessage(), e);
        }
    }

    private String determinarResourceType(String publicIdConRuta) {
        if (publicIdConRuta.contains("/videos/") || publicIdConRuta.contains("/audios/")) {
            return "video";
        } else if (publicIdConRuta.contains("/documentos/")) {
            return "raw";
        }
        return "image";
    }

    private String determinarCarpeta(String extension) {
        switch (extension) {
            // IMÁGENES -> incidentes/imagenes
            case "jpg": case "jpeg": case "png": case "gif": case "webp": case "bmp": case "svg":
                return "imagenes";

            // AUDIOS -> incidentes/audios
            case "mp3": case "wav": case "ogg": case "m4a": case "aac": case "flac": case "wma":
                return "audios";

            // VIDEOS -> incidentes/videos
            case "mp4": case "avi": case "mov": case "mkv": case "webm": case "wmv": case "flv": case "3gp":
                return "videos";

            // DOCUMENTOS -> incidentes/documentos
            case "pdf": case "doc": case "docx": case "xls": case "xlsx": case "ppt": case "pptx":
            case "txt": case "csv": case "rtf": case "zip": case "rar": case "7z":
            default:
                return "documentos";
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        int lastIndex = nombreArchivo.lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(lastIndex + 1);
        }
        return "";
    }
}