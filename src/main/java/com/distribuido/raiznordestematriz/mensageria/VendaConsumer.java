package com.distribuido.raiznordestematriz.mensageria;

import com.distribuido.raiznordestematriz.consolidacao.ConsolidacaoService;
import com.distribuido.raiznordestematriz.mensageria.evento.VendaEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class VendaConsumer {

    private static final Logger log = LoggerFactory.getLogger(VendaConsumer.class);
    private final ConsolidacaoService service;

    public VendaConsumer(ConsolidacaoService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.FILA_VENDAS)
    public void consumir(VendaEvento evento) {
        try {
            service.consolidar(evento);
        } catch (DataIntegrityViolationException e) {
            // pedidoOrigemId já existe: duplicata -> ignora e confirma (ACK)
            log.info("Venda {} já consolidada, ignorando duplicata", evento.pedidoOrigemId());
        }
    }
}
