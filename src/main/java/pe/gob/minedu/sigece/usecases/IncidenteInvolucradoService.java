package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.RolInvolucrado;
import pe.edu.cibertec.sigece.service.dto.InvolucradoDTO;

import java.util.List;
import java.util.UUID;

/**
 * Gestiona el mapeo de actores (víctimas, agresores, testigos) dentro
 * de cada expediente, soportando tanto violencia entre pares (alumno-alumno)
 * como de personal hacia alumno.
 */
public interface IncidenteInvolucradoService {

    /**
     * Vincula a un estudiante o a un miembro del personal como parte
     * involucrada en el incidente. Valida la regla de exclusividad
     * (estudiante XOR usuario) antes de persistir.
     */
    InvolucradoDTO agregarInvolucrado(UUID incidenteId, InvolucradoDTO dto, Long usuarioId);

    /**
     * Remueve a un involucrado registrado erróneamente (antes del cierre del caso).
     */
    void removerInvolucrado(Long involucradoId, Long usuarioId);

    /**
     * Lista todos los involucrados de un incidente, agrupados por rol
     * (VICTIMA, AGRESOR, TESTIGO).
     */
    List<InvolucradoDTO> listarPorIncidente(UUID incidenteId);

    /**
     * Lista todos los estudiantes que han figurado con un rol específico
     * (p. ej. AGRESOR) en múltiples incidentes, insumo clave para el
     * análisis de reincidencia y derivación a psicología.
     */
    List<InvolucradoDTO> listarPorEstudianteYRol(Long estudianteId, RolInvolucrado rol);
}