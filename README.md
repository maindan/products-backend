# Products - Backend

Este é o core da aplicação Products Backend, responsável pela lógica de negócio, persistência de dados e inteligência para gestão de produção. O sistema foi desenvolvido seguindo os princípios de robustez e escalabilidade da plataforma Java.

## 🚀 Tecnologias Utilizadas

O backend foi construído com as seguintes tecnologias:

- Java 17+: Linguagem base para o desenvolvimento.
- Spring Boot: Framework principal para criação da API REST.
- Spring Data JPA: Abstração de persistência e gerenciamento do ORM.
- PostgreSQL: Banco de dados relacional para armazenamento seguro dos dados.
- Liquibase: Gerenciamento de versionamento e migrações do banco de dados (schema).
- Docker: Containerização para garantir paridade entre ambientes de desenvolvimento e produção.
- Maven: Gerenciador de dependências e build.

## ✨ Funcionalidades da API

### Gestão de Dados (CRUDs)
- API completa para gerenciamento de Produtos e Materiais (insumos).
- Endpoints otimizados para busca e filtragem.

### Inteligência de Negócio
- Composição de Custos: Lógica para calcular o custo total de um produto baseado nos seus insumos.
- Algoritmo de Sugestão de Produção: Calcula a viabilidade de fabricação cruzando dados de estoque atual e receitas cadastradas.

### Segurança e Infraestrutura
- Migrações Automáticas: Evolução do banco de dados via Liquibase.
- Isolamento de Ambiente: Configuração pronta para rodar via Docker Compose.

## 🛠️ Como Executar

Para rodar o backend localmente:

1. Requisitos:
   - Ter o Docker e Docker Compose instalados.
   - Java 17 instalado (caso queira rodar sem Docker).

2. Inicie o banco de dados via Docker:
   docker-compose up -d db

3. Execute a aplicação:
   ./mvnw spring-boot:run

A API estará disponível em: http://localhost:8080

## 📂 Estrutura do Projeto

- /src/main/java/.../controller: Endpoints da API.
- /src/main/java/.../service: Regras de negócio e lógica de sugestão de produção.
- /src/main/java/.../model: Entidades JPA (Produtos, Materiais, Insumos).
- /src/main/resources/db/changelog: Scripts de migração do Liquibase.
