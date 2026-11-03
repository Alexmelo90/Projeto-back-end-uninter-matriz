package com.distribuido.raiznordestematriz.cliente;

import com.distribuido.raiznordestematriz.cliente.model.ConsentimentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentimentoRepository extends JpaRepository<ConsentimentoModel, Long> {
}
