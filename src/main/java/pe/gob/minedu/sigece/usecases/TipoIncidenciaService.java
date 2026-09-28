package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.CategoriaSiseve;
import pe.edu.cibertec.sigece.service.dto.TipoIncidenciaDTO;

import java.util.List;

/**
 * Administra el catálogo homologado de tipologías de violencia escolar,
 * alineado a las categorías oficiales de SíSeVe, garantizando una
 * clasificación estandarizada y sin ambigüedad técnica o legal.
 */
public interface TipoIncidenciaService {

    TipoIncidenciaDTO crear(TipoIncidenciaDTO dto);

    TipoIncidenciaDTO actualizar(Long id, TipoIncidenciaDTO dto);

    void desactivar(Long id);

    List<TipoIncidenciaDTO> listarActivos();

    List<TipoIncidenciaDTO> listarPorCategoriaSiseve(CategoriaSiseve categoria);
}