package com.distribuido.raiznordestematriz.campanha;

import com.distribuido.raiznordestematriz.campanha.model.CampanhaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampanhaRepository extends JpaRepository<CampanhaModel, Long> {
}
