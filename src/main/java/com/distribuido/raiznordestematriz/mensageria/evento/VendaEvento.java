package com.distribuido.raiznordestematriz.mensageria.evento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Cópia idêntica à do nó. Publicado pelo nó, consumido pela matriz. */
public record VendaEvento(
        UUID pedidoOrigemId,
        Long unidadeId,             // id da unidade NA MATRIZ
        UUID clienteUuid,           // nulo = venda anônima ou cliente sem consentimento de fidelidade
        String tipoOrigem,
        BigDecimal total,
        LocalDateTime data,
        List<Item> itens) {

    public record Item(Long produtoId, int qtd, BigDecimal valor) { }   // valor = preço unitário
}
