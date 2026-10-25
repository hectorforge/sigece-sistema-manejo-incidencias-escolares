package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaRequestDto;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;

import java.util.List;

@Tag(name = "Aulas de tutores", description = "Asignación de nivel, grado y sección a tutores (base de RN04)")
@RequestMapping("/api/v1/tutor-aulas")
public interface ITutorAulaContractApi {

    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Asignar aula a un tutor", description = "Rol: ADMINISTRADOR. Si la asignación existía dada de baja, se reactiva.")
    @PostMapping
    ResponseEntity<ApiResponse<TutorAulaResponseDto>> asignarAula(
            @Valid @RequestBody TutorAulaRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Aulas de un tutor", description = "Con anioEscolar lista solo las activas de ese año; sin él, todo el historial.")
    @GetMapping("/tutor/{usuarioId}")
    ResponseEntity<ApiResponse<List<TutorAulaResponseDto>>> listarPorTutor(
            @PathVariable Long usuarioId, @RequestParam(required = false) Integer anioEscolar, HttpServletRequest request);

    @Operation(summary = "Tutores de un aula")
    @GetMapping("/aula")
    ResponseEntity<ApiResponse<List<TutorAulaResponseDto>>> listarTutoresPorAula(
            @RequestParam Nivel nivel, @RequestParam String grado, @RequestParam String seccion,
            @RequestParam(required = false) Integer anioEscolar, HttpServletRequest request);

    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Desactivar asignación")
    @PatchMapping("/{tutorAulaId}/desactivar")
    ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable Long tutorAulaId, HttpServletRequest request);

    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Reactivar asignación")
    @PatchMapping("/{tutorAulaId}/reactivar")
    ResponseEntity<ApiResponse<Void>> reactivar(@PathVariable Long tutorAulaId, HttpServletRequest request);
}