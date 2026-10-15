package pe.gob.minedu.sigece.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.controllers.contracts.IApoderadoContractApi;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.usecases.IApoderadoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apoderados")
@RequiredArgsConstructor
public class ApoderadoController implements IApoderadoContractApi {
    private final IApoderadoService apoderadoService;


    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> registrarApoderado(ApoderadoRequestDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorId(Long apoderadoId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorNumeroDocumento(String numeroDocumento) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> listarPorEstudiante(Long estudianteId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<List<VinculoFamiliarResponseDto>>> listarVinculosPorApoderado(Long apoderadoId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> buscar(String apellido, String nombre, String numeroDocumento, int page, int size) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> marcarContactoPrincipal(Long estudianteId, Long apoderadoId) {
        return null;
    }
}
