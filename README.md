Raízes do Nordeste — Matriz
API da franqueadora da rede Raízes do Nordeste. É a fonte da verdade de regiões, unidades, catálogo de produtos e programa de fidelidade. Consome as vendas pagas enviadas pelos nós regionais pela fila `vendas`, consolida os relatórios e publica produtos e unidades para os nós pela fila `catalogo`.
Este projeto trabalha em conjunto com o nó regional (`raiz-nordeste-regional`, porta 8081). Os dois se comunicam apenas pelo RabbitMQ e por REST, e cada um tem o seu próprio banco.
Tecnologias e versões
Tecnologia	Versão	Uso no projeto
Java	21	Linguagem
Spring Boot	4.1.2	Base da aplicação (Web MVC, Validation, Actuator, DevTools)
Spring Data JPA / Hibernate	gerenciada pelo Spring Boot 4.1.2	Persistência
Spring AMQP	gerenciada pelo Spring Boot 4.1.2	Integração com o RabbitMQ
PostgreSQL	16 (imagem `postgres:16-alpine`)	Banco de dados
RabbitMQ	3 (imagem `rabbitmq:3-management`)	Mensageria entre a matriz e os nós
springdoc-openapi	3.2.0	Documentação Swagger / OpenAPI
Lombok	gerenciada pelo Spring Boot 4.1.2	Redução de código repetitivo
Maven	3.10.0 (via Maven Wrapper 3.3.4)	Build
Docker / Docker Compose	—	PostgreSQL e RabbitMQ locais
Infraestrutura com Docker Compose (PostgreSQL e RabbitMQ)
Os dois projetos (matriz e nó regional) usam o mesmo PostgreSQL e o mesmo RabbitMQ. Basta subir os containers uma vez.
Crie um arquivo `docker-compose.yml` com o conteúdo abaixo:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: postgres_db
    restart: always
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    restart: always
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: "123456"
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

volumes:
  postgres_data:
  rabbitmq_data:
```
Suba os containers:
```bash
docker compose up -d
```
Crie os bancos de dados (uma única vez). Este projeto usa o banco `matriz`; o outro projeto usa `no_regional`:
```bash
docker exec postgres_db psql -U postgres -c "CREATE DATABASE matriz"
docker exec postgres_db psql -U postgres -c "CREATE DATABASE no_regional"
```
Serviço	Endereço	Usuário / senha
PostgreSQL	`localhost:5432`	`postgres` / `postgres`
RabbitMQ (AMQP)	`localhost:5672`	`admin` / `123456`
RabbitMQ (painel de gestão)	http://localhost:15672	`admin` / `123456`
As tabelas são criadas automaticamente pelo JPA (`spring.jpa.hibernate.ddl-auto=update`) na primeira execução. As filas `vendas`, `vendas.dlq`, `catalogo` e `catalogo.dlq` são declaradas pelas próprias aplicações ao subirem.
Configuração (`application.properties`)
As configurações ficam em `src/main/resources/application.properties`:
```properties
spring.application.name=raiz-nordeste-matriz
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/matriz
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=123456
spring.rabbitmq.virtual-host=/
spring.rabbitmq.listener.simple.default-requeue-rejected=false

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
spring.devtools.livereload.port=35729
```
Para ajustar ao seu ambiente, há duas formas:
Editar o `application.properties` diretamente.
Usar variáveis de ambiente, sem alterar o arquivo. O Spring Boot converte cada propriedade para maiúsculas, troca `.` e `-` por `_` e dá prioridade à variável de ambiente sobre o arquivo:
Propriedade	Variável de ambiente	Valor padrão	Descrição
`server.port`	`SERVER_PORT`	`8080`	Porta HTTP da matriz
`spring.datasource.url`	`SPRING_DATASOURCE_URL`	`jdbc:postgresql://localhost:5432/matriz`	Banco da matriz
`spring.datasource.username`	`SPRING_DATASOURCE_USERNAME`	`postgres`	Usuário do PostgreSQL
`spring.datasource.password`	`SPRING_DATASOURCE_PASSWORD`	`postgres`	Senha do PostgreSQL
`spring.rabbitmq.host`	`SPRING_RABBITMQ_HOST`	`localhost`	Host do RabbitMQ
`spring.rabbitmq.port`	`SPRING_RABBITMQ_PORT`	`5672`	Porta AMQP do RabbitMQ
`spring.rabbitmq.username`	`SPRING_RABBITMQ_USERNAME`	`admin`	Usuário do RabbitMQ
`spring.rabbitmq.password`	`SPRING_RABBITMQ_PASSWORD`	`123456`	Senha do RabbitMQ
Exemplo no Linux/macOS:
```bash
export SPRING_DATASOURCE_PASSWORD=minha_senha
./mvnw spring-boot:run
```
Exemplo no Windows (PowerShell):
```powershell
$env:SPRING_DATASOURCE_PASSWORD="minha_senha"
.\mvnw.cmd spring-boot:run
```
> `spring.rabbitmq.listener.simple.default-requeue-rejected=false` deve continuar `false`: é o que faz uma mensagem com erro ir para a DLQ em vez de voltar para a fila em loop.
Como rodar
Pré-requisitos: JDK 21 e Docker. Não é preciso instalar o Maven: o projeto usa o Maven Wrapper (`mvnw`).
Suba o PostgreSQL e o RabbitMQ e crie os bancos (ver seção Infraestrutura com Docker Compose).
Na pasta do projeto, execute:
```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```
A aplicação sobe na porta 8080.
A matriz pode subir antes ou depois do nó regional; as mensagens ficam guardadas nas filas até serem consumidas.
Outros comandos úteis:
```bash
./mvnw test       # executa os testes
./mvnw package    # gera o .jar em target/
java -jar target/*.jar
```
Documentação da API (Swagger)
Com a aplicação rodando:
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI (JSON): http://localhost:8080/v3/api-docs
Pelo Swagger UI é possível executar qualquer endpoint com o botão Try it out.
Principais endpoints
Método	Endpoint	Descrição
POST / GET	`/api/regioes`	Cadastrar e listar regiões
POST	`/api/regioes/{regiaoId}/unidades`	Cadastrar unidade (publica na fila `catalogo`)
GET	`/api/unidades`	Listar unidades
POST / PUT / GET	`/api/produtos`, `/api/produtos/{id}`	Manter o catálogo (publica na fila `catalogo`)
POST	`/api/campanhas`, `/api/campanhas/{id}/regioes`	Cadastrar campanha e vincular a regiões
GET	`/api/consolidacao/vendas`	Vendas consolidadas (filtros `de`, `ate`, `unidadeId`)
GET	`/api/consolidacao/resumo`	Resumo das vendas (filtros `de`, `ate`, `unidadeId`)
GET	`/fidelidade/contas/{clienteUuid}/saldo`	Saldo de pontos (usado pelo nó)
POST	`/fidelidade/contas/{clienteUuid}/resgates`	Resgate de pontos (usado pelo nó)
Mensageria
Fila	Sentido	Conteúdo
`vendas` (DLQ `vendas.dlq`)	nó → matriz	Venda paga (`VendaEvento`)
`catalogo` (DLQ `catalogo.dlq`)	matriz → nó	Produto ou unidade (`CatalogoEvento`)
