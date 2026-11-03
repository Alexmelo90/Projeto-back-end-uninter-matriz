package com.distribuido.raiznordestematriz.campanha.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InputCampanhaRegiaoDTO(
        @NotNull Long regiaoId,
        boolean ativa,
        LocalDate dataInicio,
        LocalDate dataFim) {
}
