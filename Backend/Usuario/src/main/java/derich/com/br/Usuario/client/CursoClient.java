package derich.com.br.Usuario.client;

import derich.com.br.Usuario.DTO.CursoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "curso-service")
public interface CursoClient {

    @PostMapping("/curso/buscarCursosPorIdUsuario")
    List<CursoDTO> buscarCursosPorIds(@RequestBody List<String> ids);
}

