package com.distribuido.raiznordestematriz.comum.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadados da documentação OpenAPI exibida no Swagger UI ({@code /swagger-ui.html}). */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi(@Value("${server.port}") int porta) {
        return new OpenAPI()
                .info(new Info()
                        .title("API da Matriz - Raízes do Nordeste")
                        .version("1.0")
                        .description("Fonte da verdade de regiões, unidades, catálogo e fidelidade. Consolida as vendas recebidas dos nós pela fila vendas e publica produtos e unidades na fila catalogo."))
                .servers(List.of(new Server().url("http://localhost:" + porta).description("Matriz (local)")));
    }
}
