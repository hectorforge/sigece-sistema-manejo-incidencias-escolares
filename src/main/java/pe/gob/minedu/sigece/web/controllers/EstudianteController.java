package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.apoderados.AsignarApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudiantePerfilResponseDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.usecases.IEstudianteService;
import pe.gob.minedu.sigece.web.contracts.IEstudianteContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EstudianteController implements IEstudianteContractApi {

    private final IEstudianteService estudianteService;

    @Override
    public ResponseEntity<ApiResponse<EstudianteResponseDto>> registrarEstudiante(EstudianteRequestDto dto, HttpServletRequest request) {
        EstudianteResponseDto data = estudianteService.registrarEstudiante(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Estudiante registrado exitosamente en el padrón", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<EstudianteResponseDto>> actualizarDatos(Long estudianteId, EstudianteRequestDto dto, HttpServletRequest request) {
        EstudianteResponseDto data = estudianteService.actualizarDatos(estudianteId, dto);
        return ResponseEntity.ok(ApiResponse.success(data, "Datos del estudiante actualizados correctamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<EstudianteResponseDto>> obtenerPorId(Long estudianteId, HttpServletRequest request) {
        EstudianteResponseDto data = estudianteService.obtenerPorId(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(data, "Estudiante recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<EstudianteResponseDto>> obtenerPorCodigoSiagie(String codigoSiagie, HttpServletRequest request) {
        EstudianteResponseDto data = estudianteService.obtenerPorCodigoSiagie(codigoSiagie);
        return ResponseEntity.ok(ApiResponse.success(data, "Estudiante recuperado exitosamente por código SIAGIE", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<EstudiantePerfilResponseDto>> obtenerPerfil(Long estudianteId, HttpServletRequest request) {
        EstudiantePerfilResponseDto data = estudianteService.obtenerPerfil(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(data, "Perfil integral del estudiante recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<EstudianteResponseDto>>> buscar(EstudianteFiltroRequestDto filtro, HttpServletRequest request) {
        PagedResponse<EstudianteResponseDto> response = estudianteService.buscar(filtro);
        return ResponseEntity.ok(ApiResponse.paginated(response.content(), response.meta(), "Estudiantes listados exitosamente", request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> desactivarEstudiante(Long estudianteId, HttpServletRequest request) {
        estudianteService.desactivarEstudiante(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(null, "Estudiante desactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> reactivarEstudiante(Long estudianteId, HttpServletRequest request) {
        estudianteService.reactivarEstudiante(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(null, "Estudiante reactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<VinculoFamiliarResponseDto>> asignarApoderado(Long estudianteId, AsignarApoderadoRequestDto dto, HttpServletRequest request) {
        VinculoFamiliarResponseDto data = estudianteService.asignarApoderado(estudianteId, dto);
        return ResponseEntity.ok(ApiResponse.success(data, "Apoderado asignado exitosamente al estudiante", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> desasignarApoderado(Long estudianteId, Long apoderadoId, HttpServletRequest request) {
        estudianteService.desasignarApoderado(estudianteId, apoderadoId);
        return ResponseEntity.ok(ApiResponse.success(null, "Apoderado desvinculado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Integer>> contarIncidentesAsociados(Long estudianteId, HttpServletRequest request) {
        int conteo = estudianteService.contarIncidentesAsociados(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(conteo, "Conteo de incidentes asociados obtenido exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}