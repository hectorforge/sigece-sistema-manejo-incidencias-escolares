package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;

import java.util.List;

/**
 * Gestiona a los padres/tutores legales, receptores oficiales de las
 * notificaciones y responsables civiles del menor ante la IE.
 */
public interface IApoderadoService {

    ApoderadoResponseDto registrarApoderado(ApoderadoRequestDto dto);

    ApoderadoResponseDto actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto);

    ApoderadoResponseDto obtenerPorId(Long apoderadoId);

    ApoderadoResponseDto obtenerPorNumeroDocumento(String numeroDocumento);

    /**
     * Recupera al/los apoderado(s) de un estudiante, priorizando el
     * marcado como es_contacto_principal para el envío de notificaciones.
     */
    List<ApoderadoResponseDto> listarPorEstudiante(Long estudianteId);

    /**
     * Define/reasigna cuál apoderado es el contacto principal de
     * emergencia de un estudiante determinado.
     */
    void marcarContactoPrincipal(Long estudianteId, Long apoderadoId);
}