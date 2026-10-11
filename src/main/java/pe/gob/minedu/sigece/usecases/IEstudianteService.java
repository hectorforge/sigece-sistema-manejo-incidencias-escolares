package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;


import java.util.List;

/**
 * Administra el padrón escolar protegido conforme a la Ley N.° 29733
 * (datos personales de menores de edad), como sujeto central de
 * protección dentro del sistema.
 */
public interface IEstudianteService {

    EstudianteResponseDto registrarEstudiante(EstudianteRequestDto dto);

    EstudianteResponseDto actualizarDatos(Long estudianteId, EstudianteRequestDto dto);

    EstudianteResponseDto obtenerPorId(Long estudianteId);

    EstudianteResponseDto obtenerPorCodigoSiagie(String codigoSiagie);

    List<EstudianteResponseDto> listarPorGradoYSeccion(String grado, String seccion, pe.gob.minedu.sigece.enums.Nivel nivel);

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