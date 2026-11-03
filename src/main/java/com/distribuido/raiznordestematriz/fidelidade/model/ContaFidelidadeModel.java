package com.distribuido.raiznordestematriz.fidelidade.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conta_fidelidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContaFidelidadeModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long clienteId;
    private int saldoPontos;
}
