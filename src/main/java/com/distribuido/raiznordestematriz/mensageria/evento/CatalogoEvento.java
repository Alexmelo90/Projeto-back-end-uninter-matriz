package com.distribuido.raiznordestematriz.mensageria.evento;

import java.math.BigDecimal;
import java.util.Set;

/** Cópia idêntica à do nó. Publicado pela matriz, consumido pelo nó. */
public record CatalogoEvento(
        String tipo,                       // "PRODUTO" | "UNIDADE"
        // --- tipo PRODUTO ---
        Long produtoId,
        String nome,
        String categoria,
        boolean sazonal,
        String janelaDisponibilidade,
        BigDecimal precoBase,
        Set<String> camposTravados,
        // --- tipo UNIDADE ---
        Long unidadeId,
        String unidadeNome,
        String regiao,
        String tipoCozinha) {

    public static final String PRODUTO = "PRODUTO";
    public static final String UNIDADE = "UNIDADE";
}
