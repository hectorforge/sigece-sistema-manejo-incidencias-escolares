package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.adjuntos.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Evidencias", description = "Adjuntos del expediente con hash SHA-256 y eliminación lógica")
@RequestMapping("/api/v1")
public interface IIncidenteAdjuntoContractApi {

    @Operation(summary = "Subir evidencia", description = "multipart/form-data: parte 'datos' (JSON) y parte 'archivo'. Máximo 10 MB.")
    @PreAuthorize("hasAnyRole('TUTOR', 'PSICOLOGO', 'ADMINISTRADOR')")
    @PostMapping(value = "/incidentes/{incidenteId}/adjuntos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<AdjuntoResponseDto>> subirEvidencia(
            @PathVariable UUID incidenteId,
            @Valid @RequestPart("datos") AdjuntoRequestDto dto,
            @RequestPart("archivo") MultipartFile archivo,
            HttpServletRequest request);

    @Operation(summary = "Subir varias evidencias", description = "Una sola descripción aplica a todos los archivos del lote.")
    @PreAuthorize("hasAnyRole('TUTOR', 'PSICOLOGO', 'ADMINISTRADOR')")
    @PostMapping(value = "/incidentes/{incidenteId}/adjuntos/lote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<List<AdjuntoResponseDto>>> subirEvidencias(
            @PathVariable UUID incidenteId,
            @Valid @RequestPart("datos") AdjuntoRequestDto dto,
            @RequestPart("archivos") List<MultipartFile> archivos,
            HttpServletRequest request);

    @Operation(summary = "Listar evidencias activas del incidente")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/incidentes/{incidenteId}/adjuntos")
    ResponseEntity<ApiResponse<List<AdjuntoResponseDto>>> listarPorIncidente(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    @Operation(summary = "URL temporal de descarga", description = "Expira entre 1 y 60 minutos (por defecto 15).")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/adjuntos/{adjuntoId}/descarga")
    ResponseEntity<ApiResponse<AdjuntoUrlResponseDto>> generarUrlDescarga(
            @PathVariable Long adjuntoId, @RequestParam(defaultValue = "15") int minutos, HttpServletRequest request);

    @Operation(summary = "Verificar integridad (SHA-256)")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO')")
    @GetMapping("/adjuntos/{adjuntoId}/integridad")
    ResponseEntity<ApiResponse<IntegridadResponseDto>> verificarIntegridad(
            @PathVariable Long adjuntoId, HttpServletRequest request);

    @Operation(summary = "Eliminar evidencia (lógica)", description = "Exige el motivo de eliminación en el cuerpo.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO')")
    @DeleteMapping("/adjuntos/{adjuntoId}")
    ResponseEntity<ApiResponse<Void>> eliminarAdjunto(
            @PathVariable Long adjuntoId, @Valid @RequestBody EliminarAdjuntoRequestDto dto, HttpServletRequest request);
}