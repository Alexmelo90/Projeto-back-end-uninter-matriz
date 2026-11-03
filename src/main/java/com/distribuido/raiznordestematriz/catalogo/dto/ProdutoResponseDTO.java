package com.distribuido.raiznordestematriz.catalogo.dto;

import com.distribuido.raiznordestematriz.catalogo.model.ProdutoModel;

import java.math.BigDecimal;
import java.util.Set;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String categoria,
        boolean sazonal,
        String janelaDisponibilidade,
        BigDecimal precoBase,
        Set<String> camposTravados) {

    public static ProdutoResponseDTO de(ProdutoModel p) {
        return new ProdutoResponseDTO(p.getId(), p.getNome(), p.getCategoria(), p.isSazonal(),
                p.getJanelaDisponibilidade(), p.getPrecoBase(), Set.copyOf(p.getCamposTravados()));
    }
}
