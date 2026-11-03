package com.distribuido.raiznordestematriz.consolidacao;

import com.distribuido.raiznordestematriz.consolidacao.model.VendaConsolidadaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaConsolidadaRepository extends JpaRepository<VendaConsolidadaModel, Long> {
    List<VendaConsolidadaModel> findByDataBetween(LocalDateTime de, LocalDateTime ate);

    List<VendaConsolidadaModel> findByUnidadeIdAndDataBetween(Long unidadeId, LocalDateTime de, LocalDateTime ate);
}
