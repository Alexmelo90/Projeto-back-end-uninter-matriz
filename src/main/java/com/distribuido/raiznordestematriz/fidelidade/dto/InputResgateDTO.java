package com.distribuido.raiznordestematriz.fidelidade.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/** {@code resgateId} é a chave de idempotência: repetir o pedido com o mesmo id não debita de novo. */
public record InputResgateDTO(@NotNull UUID resgateId, @Positive int pontos, @Size(max = 120) String descricao) {
}
