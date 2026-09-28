package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.service.dto.ApoderadoDTO;

import java.util.List;

/**
 * Gestiona a los padres/tutores legales, receptores oficiales de las
 * notificaciones y responsables civiles del menor ante la IE.
 */
public interface ApoderadoService {

    ApoderadoDTO registrarApoderado(ApoderadoDTO dto);

    ApoderadoDTO actualizarDatosContacto(Long apoderadoId, ApoderadoDTO dto);

    ApoderadoDTO obtenerPorId(Long apoderadoId);

    ApoderadoDTO obtenerPorNumeroDocumento(String numeroDocumento);

    /**
     * Recupera al/los apoderado(s) de un estudiante, priorizando el
     * marcado como es_contacto_principal para el envío de notificaciones.
     */
    List<ApoderadoDTO> listarPorEstudiante(Long estudianteId);

    /**
     * Define/reasigna cuál apoderado es el contacto principal de
     * emergencia de un estudiante determinado.
     */
    void marcarContactoPrincipal(Long estudianteId, Long apoderadoId);
}