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

---

## Estrutura do Projeto

O projeto é organizado em camadas bem definidas (api, domain e 
service), separando responsabilidades de entrada, regras 
de negócio e domínio.

---

## Decisões de Arquitetura

### Separação de Camadas

A aplicação segue uma divisão clara de responsabilidades:

- **Controller**: comunicação HTTP e validação de entrada  
- **Service**: regras de negócio e orquestração  
- **Domain / Repository**: modelo de domínio e persistência  

---

### Validação em Camadas

- **API**: valida formato e restrições básicas  
- **Domínio**: garante regras de negócio e consistência  

---

### Princípios SOLID

- **SRP**: cada classe possui uma responsabilidade  
- **OCP**: regras de negócio extensíveis sem modificar fluxo principal  
- **DIP**: dependência de abstrações  

---

## Testes Automatizados

- Testes de Controller (MockMvc)  
- Testes de Service (JUnit + Mockito)  
- Testes de contexto  

Executados automaticamente via **GitHub Actions**.

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

POST /orders

Campos:
- customerId (UUID)
- total (BigDecimal)

---

## Tratamento Global de Erros

Handler global com respostas padronizadas para:
- Erros de validação  
- Regras de negócio  
- Erros inesperados  

---

## Próximos Passos

- Consulta de pedidos  
- Evolução de regras de negócio  
- Mensageria  
- Aumento de cobertura de testes  
- OpenAPI  

---

## Autor

Jonathan Julião  
Desenvolvedor Backend | Java | Spring Boot  
