package pe.gob.minedu.sigece.usecases.report;

import pe.gob.minedu.sigece.dtos.reportes.ArchivoResponseDto;
import pe.gob.minedu.sigece.dtos.reportes.ExportarReporteRequestDto;

import java.util.List;
import java.util.Map;

public interface IReporteGeneratorPort {
    ArchivoResponseDto exportarDocumento(
            ExportarReporteRequestDto dto,
            List<Map<String, Object>> datos,
            Map<String, Object> parametrosReporte
    );
}