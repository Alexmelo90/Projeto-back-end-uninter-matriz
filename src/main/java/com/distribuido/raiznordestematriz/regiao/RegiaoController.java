package com.distribuido.raiznordestematriz.regiao;

import com.distribuido.raiznordestematriz.regiao.dto.InputRegiaoDTO;
import com.distribuido.raiznordestematriz.regiao.dto.InputUnidadeDTO;
import com.distribuido.raiznordestematriz.regiao.model.RegiaoModel;
import com.distribuido.raiznordestematriz.regiao.model.UnidadeModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RegiaoController {

    private final RegiaoService service;

    public RegiaoController(RegiaoService service) {
        this.service = service;
    }

    @PostMapping("/regioes")
    @ResponseStatus(HttpStatus.CREATED)
    public RegiaoModel criarRegiao(@Valid @RequestBody InputRegiaoDTO inputDto) {
        return service.criarRegiao(inputDto);
    }

    @GetMapping("/regioes")
    public List<RegiaoModel> listarRegioes() {
        return service.listarRegioes();
    }

    @GetMapping("/regioes/{id}")
    public RegiaoModel buscarRegiao(@PathVariable Long id) {
        return service.buscarRegiao(id);
    }

    @PostMapping("/regioes/{regiaoId}/unidades")
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadeModel criarUnidade(@PathVariable Long regiaoId, @Valid @RequestBody InputUnidadeDTO inputDto) {
        return service.criarUnidade(regiaoId, inputDto);
    }

    @GetMapping("/unidades")
    public List<UnidadeModel> listarUnidades() {
        return service.listarUnidades();
    }
}
