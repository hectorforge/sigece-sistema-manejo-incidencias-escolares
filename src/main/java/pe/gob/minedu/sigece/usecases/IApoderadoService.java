package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;

import java.util.List;

/**
 * Servicio encargado de gestionar a los padres y tutores legales de los estudiantes.
 * Casos de uso asociados: CU05, CU16.
 */
public interface IApoderadoService {

    /**
     * Registra un nuevo apoderado en la plataforma.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param dto Información personal y de contacto del apoderado.
     * @return Apoderado registrado.
     */
    ApoderadoResponseDto registrarApoderado(ApoderadoRequestDto dto);

    /**
     * Actualiza la información de contacto (teléfono, correo, dirección) de un apoderado.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param apoderadoId Identificador del apoderado.
     * @param dto         Nuevos datos de contacto.
     * @return Apoderado actualizado.
     */
    ApoderadoResponseDto actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto);

    /**
     * Consulta un apoderado por su ID primario.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param apoderadoId Identificador del apoderado.
     * @return Información del apoderado.
     */
    ApoderadoResponseDto obtenerPorId(Long apoderadoId);

    /**
     * Consulta a un apoderado por su documento nacional de identidad o carné de extranjería.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param numeroDocumento Número de documento de identidad.
     * @return Información del apoderado.
     */
    ApoderadoResponseDto obtenerPorNumeroDocumento(String numeroDocumento);

    /**
     * Lista los apoderados vinculados formalmente a un estudiante determinado.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados, CU16 · Notificar apoderados</b></p>
     * <p>Rol: Personal educativo, Personal de gestión, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @return Lista de apoderados vinculados.
     */
    List<ApoderadoResponseDto> listarPorEstudiante(Long estudianteId);

    /**
     * Lista todos los vínculos familiares (estudiantes a cargo) asociados a un apoderado.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param apoderadoId Identificador del apoderado.
     * @return Lista de vínculos y estudiantes a cargo.
     */
    List<VinculoFamiliarResponseDto> listarVinculosPorApoderado(Long apoderadoId);

    /**
     * Asigna a un apoderado específico como el contacto principal de un estudiante.
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @param apoderadoId  Identificador del apoderado que pasará a ser principal.
     */
    void marcarContactoPrincipal(Long estudianteId, Long apoderadoId);

    /**
     * Búsqueda paginada de apoderados mediante filtros (nombres, documento, etc.).
     * <p>Caso de uso: <b>CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param filtro Criterios de búsqueda y paginación.
     * @return Página de apoderados encontrados.
     */
    PagedResponse<ApoderadoResponseDto> buscar(ApoderadoFiltroRequestDto filtro);
}