package com.distribuido.raiznordestematriz.fidelidade.dto;

import java.util.UUID;

public record SaldoResponseDTO(UUID clienteUuid, int saldoPontos) {
}
