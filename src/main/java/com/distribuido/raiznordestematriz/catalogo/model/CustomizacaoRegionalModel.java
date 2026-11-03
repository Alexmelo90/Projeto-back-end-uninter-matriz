package com.distribuido.raiznordestematriz.catalogo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customizacao_regional")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CustomizacaoRegionalModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long produtoId;
    private Long regiaoId;
    private String campo;
    private String valor;
}
