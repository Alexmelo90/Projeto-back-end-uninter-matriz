package com.distribuido.raiznordestematriz.catalogo;

import com.distribuido.raiznordestematriz.catalogo.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
}
