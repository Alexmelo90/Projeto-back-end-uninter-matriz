package com.distribuido.raiznordestematriz.cliente;

import com.distribuido.raiznordestematriz.cliente.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    Optional<ClienteModel> findByUuid(UUID uuid);
}
