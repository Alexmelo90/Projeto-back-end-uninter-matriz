package com.distribuido.raiznordestematriz.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record InputProdutoDTO(
        @NotBlank String nome,
        @NotBlank String categoria,
        boolean sazonal,
        String janelaDisponibilidade,
        @NotNull @Positive BigDecimal precoBase,
        Set<String> camposTravados) {
}
