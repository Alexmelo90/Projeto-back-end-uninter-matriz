package com.distribuido.raiznordestematriz.regiao.dto;

import jakarta.validation.constraints.NotBlank;

public record InputRegiaoDTO(@NotBlank String nome) {
}
