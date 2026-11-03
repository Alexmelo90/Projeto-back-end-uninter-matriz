package com.distribuido.raiznordestematriz.fidelidade.dto;

import com.distribuido.raiznordestematriz.fidelidade.model.MovimentoPontosModel;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResgateResponseDTO(UUID resgateId, UUID clienteUuid, int pontosResgatados, int saldoPontos,
                                 String descricao, LocalDateTime data) {

    public static ResgateResponseDTO de(MovimentoPontosModel movimento, UUID clienteUuid, int saldoPontos) {
        return new ResgateResponseDTO(movimento.getResgateId(), clienteUuid, movimento.getPontos(), saldoPontos,
                movimento.getDescricao(), movimento.getData());
    }
}
