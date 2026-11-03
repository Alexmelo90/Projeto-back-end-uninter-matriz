package com.distribuido.raiznordestematriz.regiao.dto;

import com.distribuido.raiznordestematriz.regiao.model.TipoCozinha;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InputUnidadeDTO(@NotBlank String nome, @NotNull TipoCozinha tipoCozinha) {
}
