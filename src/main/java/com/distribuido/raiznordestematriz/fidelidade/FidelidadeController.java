package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.fidelidade.dto.InputResgateDTO;
import com.distribuido.raiznordestematriz.fidelidade.dto.ResgateResponseDTO;
import com.distribuido.raiznordestematriz.fidelidade.dto.SaldoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    private final FidelidadeService service;

    public FidelidadeController(FidelidadeService service) {
        this.service = service;
    }

    @GetMapping("/contas/{clienteUuid}/saldo")
    public SaldoResponseDTO saldo(@PathVariable UUID clienteUuid) {
        return service.saldo(clienteUuid);
    }

    /** 200 também na repetição com o mesmo resgateId; 422 se o saldo não cobre o resgate. */
    @PostMapping("/contas/{clienteUuid}/resgates")
    public ResgateResponseDTO resgatar(@PathVariable UUID clienteUuid, @Valid @RequestBody InputResgateDTO inputDto) {
        return service.resgatar(clienteUuid, inputDto);
    }
}
