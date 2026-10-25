package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoRequestDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradosAgrupadosResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.RemoverInvolucradoRequestDto;
import pe.gob.minedu.sigece.enums.RolInvolucrado;

import java.util.List;
import java.util.UUID;

@Tag(name = "Involucrados", description = "Víctimas, agresores y testigos de una incidencia")
@RequestMapping("/api/v1")
public interface IIncidenteInvolucradoContractApi {

    @Operation(summary = "Agregar involucrado", description = "Debe enviar estudianteId o usuarioId, no ambos. Roles: TUTOR, PSICOLOGO.")
    @PostMapping("/incidentes/{incidenteId}/involucrados")
    ResponseEntity<ApiResponse<InvolucradoResponseDto>> agregarInvolucrado(
            @PathVariable UUID incidenteId, @Valid @RequestBody InvolucradoRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Remover involucrado", description = "Exige el motivo del retiro en el cuerpo.")
    @DeleteMapping("/incidentes/{incidenteId}/involucrados/{involucradoId}")
    ResponseEntity<ApiResponse<Void>> removerInvolucrado(
            @PathVariable UUID incidenteId, @PathVariable Long involucradoId,
            @Valid @RequestBody RemoverInvolucradoRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Listar involucrados del incidente")
    @GetMapping("/incidentes/{incidenteId}/involucrados")
    ResponseEntity<ApiResponse<List<InvolucradoResponseDto>>> listarPorIncidente(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    @Operation(summary = "Involucrados agrupados por rol")
    @GetMapping("/incidentes/{incidenteId}/involucrados/agrupados")
    ResponseEntity<ApiResponse<InvolucradosAgrupadosResponseDto>> listarAgrupados(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    @Operation(summary = "Participaciones de un estudiante según su rol")
    @GetMapping("/estudiantes/{estudianteId}/involucraciones")
    ResponseEntity<ApiResponse<List<InvolucradoResponseDto>>> listarPorEstudianteYRol(
            @PathVariable Long estudianteId, @RequestParam RolInvolucrado rol, HttpServletRequest request);
}