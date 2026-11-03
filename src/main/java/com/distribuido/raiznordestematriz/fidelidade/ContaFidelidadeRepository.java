package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.fidelidade.model.ContaFidelidadeModel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContaFidelidadeRepository extends JpaRepository<ContaFidelidadeModel, Long> {
    Optional<ContaFidelidadeModel> findByClienteId(Long clienteId);

    /** SELECT ... FOR UPDATE: crédito e débito da mesma conta passam um de cada vez. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ContaFidelidadeModel c where c.clienteId = :clienteId")
    Optional<ContaFidelidadeModel> buscarParaAtualizar(@Param("clienteId") Long clienteId);
}
