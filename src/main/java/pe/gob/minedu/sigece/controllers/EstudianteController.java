package pe.gob.minedu.sigece.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.controllers.contracts.IEstudianteContractApi;
import pe.gob.minedu.sigece.usecases.IEstudianteService;

@RestController
@RequestMapping("/api/v1/estudiante")
@RequiredArgsConstructor
public class EstudianteController implements IEstudianteContractApi {
    private final IEstudianteService estudianteService;


}
