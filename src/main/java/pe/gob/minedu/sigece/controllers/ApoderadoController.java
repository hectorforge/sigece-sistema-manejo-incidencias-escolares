package pe.gob.minedu.sigece.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.controllers.contracts.IApoderadoContractApi;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.usecases.IApoderadoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apoderados")
@RequiredArgsConstructor
public class ApoderadoController implements IApoderadoContractApi {
    private final IApoderadoService apoderadoService;


    @Override
    public ResponseEntity<ApoderadoResponseDto> registrarApoderado(ApoderadoRequestDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ApoderadoResponseDto> actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ApoderadoResponseDto> obtenerPorId(Long apoderadoId) {
        return null;
    }

    @Override
    public ResponseEntity<ApoderadoResponseDto> obtenerPorNumeroDocumento(String numeroDocumento) {
        return null;
    }

    @Override
    public ResponseEntity<List<ApoderadoResponseDto>> listarPorEstudiante(Long estudianteId) {
        return null;
    }

    @Override
    public ResponseEntity<PagedResponse<ApoderadoResponseDto>> buscar(String apellido, String nombre, String numeroDocumento) {
        return null;
    }

    @Override
    public ResponseEntity<Void> marcarContactoPrincipal(Long estudianteId, Long apoderadoId) {
        return null;
    }
}
