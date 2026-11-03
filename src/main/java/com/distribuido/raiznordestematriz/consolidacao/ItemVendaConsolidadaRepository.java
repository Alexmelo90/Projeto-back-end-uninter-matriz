package com.distribuido.raiznordestematriz.consolidacao;

import com.distribuido.raiznordestematriz.consolidacao.model.ItemVendaConsolidadaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ItemVendaConsolidadaRepository extends JpaRepository<ItemVendaConsolidadaModel, Long> {
    List<ItemVendaConsolidadaModel> findByVendaIdIn(Collection<Long> vendaIds);
}
