package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.fidelidade.model.MovimentoPontosModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MovimentoPontosRepository extends JpaRepository<MovimentoPontosModel, Long> {
    Optional<MovimentoPontosModel> findByResgateId(UUID resgateId);
}
