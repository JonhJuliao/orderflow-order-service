[![CI](https://github.com/JonhJuliao/orderflow-order-service/actions/workflows/ci.yml/badge.svg)](https://github.com/JonhJuliao/orderflow-order-service/actions/workflows/ci.yml)

# Orderflow – Order Service

Serviço responsável pelo gerenciamento de pedidos (orders) dentro do ecossistema **Orderflow**.

Este projeto faz parte de um estudo prático focado em **boas práticas de desenvolvimento backend**, **arquitetura limpa**, **princípios SOLID** e **testes automatizados**, utilizando **Java** e **Spring Boot**.

---

## Objetivo do Projeto

Construir um microserviço de pedidos seguindo práticas utilizadas por **times maduros de engenharia**, com foco em:

- Código limpo e legível  
- Separação clara de responsabilidades  
- Extensibilidade sem modificação de código existente (Open/Closed Principle)  
- Validação correta entre camadas  
- Testes automatizados em múltiplos níveis  
- Ambiente reproduzível com Docker  

---

## Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.5.10  
- Spring Web  
- Spring Data JPA  
- Jakarta Bean Validation  
- PostgreSQL  
- Flyway (controle de migrations)  
- Docker e Docker Compose  
- Maven  
- JUnit 5  
- Mockito  
- MockMvc  
- Apache Kafka
- GitHub Actions

---

## Arquitetura

A aplicação segue uma organização em camadas:

- **api** → controllers e entrada HTTP
- **service** → orquestração e casos de uso
- **domain** → modelo de domínio e regras
- **messaging** → integração com Kafka

O domínio permanece desacoplado de detalhes de infraestrutura.

---

## Eventos de Domínio

O serviço publica eventos quando fatos relevantes acontecem:

- `OrderCreatedEvent`
- `OrderStatusUpdatedEvent`

Esses eventos são emitidos pelo domínio e tratados internamente via **Application Events** do Spring.

---

## Integração Assíncrona (Kafka)

Os eventos de domínio são traduzidos em eventos de integração e publicados em tópicos Kafka:

- `order.created`
- `order.status-updated`

A publicação é feita por meio de:

- Listener de eventos do Spring
- Producer Kafka isolado da camada de negócio
- Tópicos externalizados em configuração

Isso permite que outros serviços reajam aos eventos sem acoplamento direto ao serviço de pedidos.

---

## Testes Automatizados

O projeto possui testes em múltiplos níveis:

- Controller (MockMvc)
- Service (JUnit + Mockito)
- Listener Kafka
- Contexto da aplicação

Executados automaticamente via GitHub Actions.

---

## Executando o Projeto

### Pré-requisitos

- Docker  
- Docker Compose  
- Java 17+  

### Subindo o Banco de Dados

docker compose up -d

### Executando a Aplicação

./mvnw spring-boot:run

Aplicação disponível em: http://localhost:8080

### Executando os Testes

./mvnw test

---

## Endpoint Disponível

### Criar Pedido

- POST /orders
  - Campos:
    - customerId (UUID)
    - total (BigDecimal)

### Buscar pedido por ID
- GET /orders/{id}


### Listar pedidos (paginado)
- GET /orders (paginação)


### Atualizar status do pedido
- PATCH /orders/{id}/status
  - Campos:
    - status: CREATED | CANCELLED | COMPLETED

---

## Tratamento Global de Erros

Handler global com respostas padronizadas para:
- Erros de validação  
- Regras de negócio  
- Erros inesperados  

---

## Evolução planejada

Este serviço faz parte de um ecossistema em construção.

Próximos passos:

- Criação de um segundo microserviço consumidor Kafka
- Integração entre serviços via eventos
- Observabilidade e logs estruturados
- Evolução das regras de negócio

---

## Autor

Jonathan Julião  
Desenvolvedor Backend | Java | Spring Boot  
