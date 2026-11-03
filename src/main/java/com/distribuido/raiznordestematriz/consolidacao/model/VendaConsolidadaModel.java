package com.distribuido.raiznordestematriz.consolidacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "venda_consolidada")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VendaConsolidadaModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long unidadeId;
    private Long clienteId;            // nulo em venda anônima
    @Enumerated(EnumType.STRING)
    private Canal tipoOrigem;
    private BigDecimal total;
    private LocalDateTime data;
    @Column(unique = true, nullable = false)
    private UUID pedidoOrigemId;
}
