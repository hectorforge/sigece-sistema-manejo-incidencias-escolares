package pe.gob.minedu.sigece.usecases.report;

import org.springframework.stereotype.Service;
import pe.gob.minedu.sigece.dtos.reportes.ArchivoResponseDto;
import pe.gob.minedu.sigece.dtos.reportes.ExportarReporteRequestDto;

import java.util.List;
import java.util.Map;

@Service
public class ReportGeneratorPort implements IReporteGeneratorPort{
    @Override
    public ArchivoResponseDto exportarDocumento(ExportarReporteRequestDto dto, List<Map<String, Object>> datos, Map<String, Object> parametrosReporte) {
        return null;
    }
}
