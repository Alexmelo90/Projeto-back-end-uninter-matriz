package com.distribuido.raiznordestematriz.regiao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regiao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegiaoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}
