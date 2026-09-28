package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.Nivel;
import pe.edu.cibertec.sigece.service.dto.EstudianteDTO;

import java.util.List;

/**
 * Administra el padrón escolar protegido conforme a la Ley N.° 29733
 * (datos personales de menores de edad), como sujeto central de
 * protección dentro del sistema.
 */
public interface EstudianteService {

    EstudianteDTO registrarEstudiante(EstudianteDTO dto);

    EstudianteDTO actualizarDatos(Long estudianteId, EstudianteDTO dto);

    EstudianteDTO obtenerPorId(Long estudianteId);

    EstudianteDTO obtenerPorCodigoSiagie(String codigoSiagie);

    List<EstudianteDTO> listarPorGradoYSeccion(String grado, String seccion, Nivel nivel);

    /**
     * Desactiva (soft-delete) a un estudiante egresado o retirado,
     * preservando el historial de incidentes para fines estadísticos.
     */
    void desactivarEstudiante(Long estudianteId);

    /**
     * Vincula a un apoderado con un estudiante, definiendo el parentesco
     * y si constituye el contacto principal para notificaciones.
     */
    void asignarApoderado(Long estudianteId, Long apoderadoId, String parentesco, boolean esContactoPrincipal);

    /**
     * Cuenta el número de incidentes registrados en los que el estudiante
     * ha participado (en cualquier rol), insumo para el semáforo de
     * reincidencia mostrado en el perfil del alumno.
     */
    int contarIncidentesAsociados(Long estudianteId);
}