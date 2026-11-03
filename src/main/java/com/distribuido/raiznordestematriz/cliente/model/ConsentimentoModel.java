package com.distribuido.raiznordestematriz.cliente.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consentimento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ConsentimentoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private String finalidade;
    private String status;
    private LocalDateTime data;
}
