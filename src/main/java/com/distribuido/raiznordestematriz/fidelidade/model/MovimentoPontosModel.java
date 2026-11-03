package com.distribuido.raiznordestematriz.fidelidade.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimento_pontos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MovimentoPontosModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contaId;
    /** Preenchido só no CREDITO (venda que gerou os pontos). */
    private Long vendaId;
    private int pontos;
    @Enumerated(EnumType.STRING)
    private TipoMovimento tipo;
    private LocalDateTime data;
    /** Chave de idempotência do DEBITO, gerada por quem pede o resgate. Nula no CREDITO. */
    @Column(unique = true)
    private UUID resgateId;
    private String descricao;
}
