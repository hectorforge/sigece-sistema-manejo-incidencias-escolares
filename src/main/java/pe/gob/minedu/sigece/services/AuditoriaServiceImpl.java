package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaRequestDto;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;
import pe.gob.minedu.sigece.mappers.AuditoriaMapper;
import pe.gob.minedu.sigece.models.AuditoriaEvento;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IAuditoriaEventoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.persistence.specs.AuditoriaEventoSpecification;
import pe.gob.minedu.sigece.usecases.IAuditoriaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements IAuditoriaService {

    private final IAuditoriaEventoRepository auditoriaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IIncidenteRepository incidenteRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(AuditoriaRequestDto dto) {
        Usuario usuario = dto.usuarioId() != null
                ? usuarioRepository.findById(dto.usuarioId()).orElse(null)
                : null;

        Incidente incidente = dto.incidenteId() != null
                ? incidenteRepository.findById(dto.incidenteId()).orElse(null)
                : null;

        AuditoriaEvento a = new AuditoriaEvento();
        a.setUsuario(usuario);
        a.setUsernameIntentado(dto.usernameIntentado());
        a.setAccion(dto.accion());
        a.setEntidad(dto.entidad());
        a.setEntidadId(dto.entidadId());
        a.setIncidente(incidente);
        a.setValorAnterior(dto.valorAnterior());
        a.setValorNuevo(dto.valorNuevo());
        a.setDescripcion(dto.descripcion());
        a.setIpOrigen(dto.ipOrigen());
        a.setUserAgent(dto.userAgent());
        a.setResultado(dto.resultado());

        auditoriaRepository.save(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaResponseDto> listarPorEntidad(String entidad, String entidadId) {
        return auditoriaRepository.findByEntidadAndEntidadIdOrderByFechaEventoDesc(EntidadAuditoria.valueOf(entidad), entidadId)
                .stream()
                .map(AuditoriaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AuditoriaResponseDto> buscar(AuditoriaFiltroRequestDto filtro) {
        int pageNumber = Optional.ofNullable(filtro).map(AuditoriaFiltroRequestDto::page).orElse(0);
        int pageSize = Optional.ofNullable(filtro).map(AuditoriaFiltroRequestDto::size).orElse(10);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = auditoriaRepository.findAll(AuditoriaEventoSpecification.conFiltro(filtro), pageable);
        var content = page.map(AuditoriaMapper::toDto).getContent();
        var meta = new PaginationMeta(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());

        return new PagedResponse<>(content, meta);
    }
}