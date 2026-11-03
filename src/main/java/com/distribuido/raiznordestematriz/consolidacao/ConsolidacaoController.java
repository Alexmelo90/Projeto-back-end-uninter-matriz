package com.distribuido.raiznordestematriz.consolidacao;

import com.distribuido.raiznordestematriz.consolidacao.dto.ResumoVendasResponseDTO;
import com.distribuido.raiznordestematriz.consolidacao.model.VendaConsolidadaModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consolidacao")
public class ConsolidacaoController {

    private final ConsolidacaoService service;

    public ConsolidacaoController(ConsolidacaoService service) {
        this.service = service;
    }

    @GetMapping("/vendas")
    public List<VendaConsolidadaModel> vendas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate,
            @RequestParam(required = false) Long unidadeId) {
        return service.listarVendas(de, ate, unidadeId);
    }

    @GetMapping("/resumo")
    public ResumoVendasResponseDTO resumo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate,
            @RequestParam(required = false) Long unidadeId) {
        return service.resumo(de, ate, unidadeId);
    }
}
