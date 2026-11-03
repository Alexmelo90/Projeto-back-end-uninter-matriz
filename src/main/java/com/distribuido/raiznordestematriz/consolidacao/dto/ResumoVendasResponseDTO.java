package com.distribuido.raiznordestematriz.consolidacao.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record ResumoVendasResponseDTO(
        LocalDateTime de,
        LocalDateTime ate,
        Long unidadeId,
        long quantidadeVendas,
        long itensVendidos,
        BigDecimal valorTotal,
        BigDecimal ticketMedio,
        Map<String, Long> vendasPorCanal) {
}
