package com.distribuido.raiznordestematriz.mensageria;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String FILA_VENDAS = "vendas";
    public static final String FILA_VENDAS_DLQ = "vendas.dlq";
    public static final String FILA_CATALOGO = "catalogo";
    public static final String FILA_CATALOGO_DLQ = "catalogo.dlq";

    @Bean public Queue vendas()      { return comDlq(FILA_VENDAS, FILA_VENDAS_DLQ); }
    @Bean public Queue vendasDlq()   { return QueueBuilder.durable(FILA_VENDAS_DLQ).build(); }
    @Bean public Queue catalogo()    { return comDlq(FILA_CATALOGO, FILA_CATALOGO_DLQ); }
    @Bean public Queue catalogoDlq() { return QueueBuilder.durable(FILA_CATALOGO_DLQ).build(); }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    private Queue comDlq(String fila, String dlq) {
        return QueueBuilder.durable(fila)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlq)
                .build();
    }
}
