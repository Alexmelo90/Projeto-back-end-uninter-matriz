package com.distribuido.raiznordestematriz.mensageria;

import com.distribuido.raiznordestematriz.catalogo.model.ProdutoModel;
import com.distribuido.raiznordestematriz.mensageria.evento.CatalogoEvento;
import com.distribuido.raiznordestematriz.regiao.model.RegiaoModel;
import com.distribuido.raiznordestematriz.regiao.model.UnidadeModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CatalogoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CatalogoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarProduto(ProdutoModel produto) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_CATALOGO, new CatalogoEvento(
                CatalogoEvento.PRODUTO, produto.getId(), produto.getNome(), produto.getCategoria(), produto.isSazonal(),
                produto.getJanelaDisponibilidade(), produto.getPrecoBase(), produto.getCamposTravados(),
                null, null, null, null));
    }

    public void publicarUnidade(UnidadeModel unidade, RegiaoModel regiao) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_CATALOGO, new CatalogoEvento(
                CatalogoEvento.UNIDADE, null, null, null, false, null, null, null,
                unidade.getId(), unidade.getNome(), regiao.getNome(), unidade.getTipoCozinha().name()));
    }
}
