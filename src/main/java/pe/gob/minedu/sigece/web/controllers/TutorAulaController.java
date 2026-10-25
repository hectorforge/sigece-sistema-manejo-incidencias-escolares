package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaRequestDto;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.usecases.ITutorAulaService;
import pe.gob.minedu.sigece.web.contracts.ITutorAulaContractApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TutorAulaController extends BaseController implements ITutorAulaContractApi {

    private final ITutorAulaService tutorAulaService;

    @Override
    public ResponseEntity<ApiResponse<TutorAulaResponseDto>> asignarAula(TutorAulaRequestDto dto, HttpServletRequest request) {
        var data = tutorAulaService.asignarAula(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data,
                "Aula asignada exitosamente al tutor", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TutorAulaResponseDto>>> listarPorTutor(Long usuarioId, Integer anioEscolar, HttpServletRequest request) {
        return ok(tutorAulaService.listarPorTutor(usuarioId, anioEscolar), "Aulas del tutor listadas exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<TutorAulaResponseDto>>> listarTutoresPorAula(Nivel nivel, String grado, String seccion, Integer anioEscolar, HttpServletRequest request) {
        return ok(tutorAulaService.listarTutoresPorAula(nivel, grado, seccion, anioEscolar), "Tutores del aula listados exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> desactivar(Long tutorAulaId, HttpServletRequest request) {
        tutorAulaService.desactivar(tutorAulaId);
        return ok(null, "Asignación desactivada exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> reactivar(Long tutorAulaId, HttpServletRequest request) {
        tutorAulaService.reactivar(tutorAulaId);
        return ok(null, "Asignación reactivada exitosamente", request);
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(data, msg, HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }
}
