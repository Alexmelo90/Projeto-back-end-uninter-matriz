package com.distribuido.raiznordestematriz.catalogo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProdutoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private boolean sazonal;
    private String janelaDisponibilidade;
    private BigDecimal precoBase;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "produto_campos_travados", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "campo")
    private Set<String> camposTravados = new HashSet<>();
}
