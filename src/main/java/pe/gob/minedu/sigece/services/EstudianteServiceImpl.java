package pe.gob.minedu.sigece.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.apoderados.AsignarApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudiantePerfilResponseDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.mappers.EstudianteMapper;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteRepository;
import pe.gob.minedu.sigece.usecases.IEstudianteService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements IEstudianteService {

    private final IEstudianteRepository estudianteRepository;


    @Override
    public EstudianteResponseDto registrarEstudiante(EstudianteRequestDto dto) {
        return null;
    }

    @Override
    public EstudianteResponseDto actualizarDatos(Long estudianteId, EstudianteRequestDto dto) {
        return null;
    }

    @Override
    public EstudianteResponseDto obtenerPorId(Long estudianteId) {
        return null;
    }

    @Override
    public EstudianteResponseDto obtenerPorCodigoSiagie(String codigoSiagie) {
        return null;
    }

    @Override
    public EstudiantePerfilResponseDto obtenerPerfil(Long estudianteId) {
        return null;
    }

    @Override
    public PagedResponse<EstudianteResponseDto> buscar(EstudianteFiltroRequestDto filtro) {
        return null;
    }

    @Override
    public void desactivarEstudiante(Long estudianteId) {

    }

    @Override
    public void reactivarEstudiante(Long estudianteId) {

    }

    @Override
    public VinculoFamiliarResponseDto asignarApoderado(Long estudianteId, AsignarApoderadoRequestDto dto) {
        return null;
    }

    @Override
    public void desasignarApoderado(Long estudianteId, Long apoderadoId) {

    }

    @Override
    public int contarIncidentesAsociados(Long estudianteId) {
        return 0;
    }
}