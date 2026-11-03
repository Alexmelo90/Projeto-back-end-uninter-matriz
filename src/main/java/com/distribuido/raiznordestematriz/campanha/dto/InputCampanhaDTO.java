package com.distribuido.raiznordestematriz.campanha.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record InputCampanhaDTO(
        @NotBlank String nome,
        String regra,
        BigDecimal desconto,
        String periodo,
        String publicoAlvo) {
}
