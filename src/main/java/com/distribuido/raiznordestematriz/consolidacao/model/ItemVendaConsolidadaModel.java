package com.distribuido.raiznordestematriz.consolidacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_venda_consolidada")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ItemVendaConsolidadaModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vendaId;
    private Long produtoId;
    private int qtd;
    private BigDecimal valor;          // preço unitário
}
