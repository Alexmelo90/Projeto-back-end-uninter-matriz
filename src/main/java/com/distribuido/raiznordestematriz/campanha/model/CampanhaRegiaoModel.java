package com.distribuido.raiznordestematriz.campanha.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "campanha_regiao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CampanhaRegiaoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long campanhaId;
    private Long regiaoId;
    private boolean ativa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
