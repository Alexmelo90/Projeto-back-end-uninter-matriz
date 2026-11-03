package com.distribuido.raiznordestematriz.campanha.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "campanha")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CampanhaModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String regra;
    private BigDecimal desconto;
    private String periodo;
    private String publicoAlvo;
}
