package com.distribuido.raiznordestematriz.cliente.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClienteModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private UUID uuid;
    // na matriz os dados pessoais ficam nulos (registro mínimo, só com o uuid)
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
}
