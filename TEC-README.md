# TEC-README — Documentação Técnica: HelpDeskTickets

## 1. Visão Geral

HelpDeskTickets é uma API RESTful para gerenciamento de chamados de suporte técnico. Construída com Spring Boot 4, oferece autenticação stateless via JWT (RS256), controle de acesso granular por roles (RBAC) e persistência em PostgreSQL via JPA/Hibernate.

---

## 2. Stack Tecnológica

| Componente | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java | 25 |
| Framework | Spring Boot | 4.0.6 |
| Segurança | Spring Security + OAuth2 Resource Server | (BOM Spring Boot 4) |
| JWT | JJWT (io.jsonwebtoken) | 0.12.7 |
| Persistência | Spring Data JPA + Hibernate | (BOM Spring Boot 4) |
| Banco de dados | PostgreSQL | latest (Docker) |
| Documentação API | SpringDoc OpenAPI (Swagger UI) | 3.0.2 |
| Build | Maven Wrapper (mvnw) | 3.9+ |
| Utilitários | Lombok 1.18.46, Apache Commons Lang3, Commons Text 1.13.0 |
| Containerização | Docker Compose | — |
| E-mail | Spring Boot Mail Starter | (BOM Spring Boot 4) |

---

## 3. Arquitetura

### 3.1 Padrão de Camadas

```
┌─────────────────────────────────────────────┐
│              Controllers (REST)             │  ← Recebe requisições HTTP, valida DTOs
├─────────────────────────────────────────────┤
│         Services (Interfaces + Impls)       │  ← Regras de negócio, autorização contextual
├─────────────────────────────────────────────┤
│              Repositories (JPA)             │  ← Acesso a dados via Spring Data
├─────────────────────────────────────────────┤
│           Entities / Domain Model           │  ← Mapeamento JPA, enums de domínio
└─────────────────────────────────────────────┘
```

### 3.2 Estrutura de Pacotes

```
br.com.doubletelecom.help_desk_tickets
└── app
    ├── configurations/       # Beans de configuração (Security, Swagger, i18n, AdminUser)
    ├── controllers/          # REST Controllers (8 controllers)
    ├── domain/
    │   ├── dtos/             # Records de entrada e saída (CreateXxx, XxxDto, PageItemXxx)
    │   └── entities/         # Entidades JPA (8 entidades)
    ├── exceptions/
    │   ├── business/         # Exceções de domínio (ObjectNotFound, Unauthorized, etc.)
    │   ├── contract/         # Interface MessageException
    │   └── dtos/             # ApiErrorDto, ErrorDto
    ├── repositories/         # Interfaces Spring Data JPA (8 repositórios)
    ├── security/             # JWTUtils — geração e validação de tokens
    └── services/
        ├── (interfaces)      # Contratos de serviço
        └── implementations/  # Implementações concretas
```

### 3.3 Padrões Aplicados

- **Interface + Implementação nos Services**: cada serviço tem uma interface e uma classe `*Impl`, facilitando testes e substituição.
- **DTO Pattern**: separação clara entre entidades JPA e objetos de transferência. DTOs de criação (`CreateXxxDto`), de leitura paginada (`PageItemXxxDto`) e de atualização (`XxxDto`).
- **Global Exception Handler**: `ExceptionHandlerAdvice` com `@ControllerAdvice` centraliza o tratamento de erros e retorna `ApiErrorDto` padronizado.
- **RBAC via `@PreAuthorize`**: autorização declarativa em cada endpoint com escopos JWT.
- **Auditoria automática via TicketLog**: toda operação relevante em tickets gera um registro de log automaticamente no service.

---

## 4. Modelo de Dados

### 4.1 Diagrama de Entidades (simplificado)

```
┌──────────┐     N:N    ┌──────────┐
│   User   │────────────│   Role   │
│          │            └──────────┘
│          │     N:N    ┌──────────┐
│          │────────────│  Group   │
└────┬─────┘            └────┬─────┘
     │                       │
     │ 1:N                   │ 1:N (destinationGroup)
     ▼                       ▼
┌──────────┐     N:1   ┌──────────────┐
│  Ticket  │───────────│TicketCategory│
│          │           └──────────────┘
│          │ N:1 (attribuitedToUser)
│          │──────────► User
└────┬─────┘
     │ 1:N          1:N
     ├──────────► TicketLog
     └──────────► TicketMessage
```

### 4.2 Tabelas do Banco

| Tabela | Entidade | Descrição |
|---|---|---|
| `tb_users` | `User` | Usuários do sistema |
| `tb_roles` | `Role` | Perfis de acesso |
| `tb_groups` | `Group` | Grupos de atendimento |
| `tb_users_roles` | (join) | Relação N:N usuário-role |
| `tb_users_groups` | (join) | Relação N:N usuário-grupo |
| `tb_tickets` | `Ticket` | Chamados de suporte |
| `tb_ticket_category` | `TicketCategory` | Categorias de chamados |
| `tb_ticket_logs` | `TicketLog` | Histórico de alterações |
| `tb_ticket_messages` | `TicketMessage` | Mensagens em chamados |
| `tb_refresh_tokens` | `RefreshToken` | Tokens de renovação JWT |

### 4.3 Enums de Domínio

**Status do Ticket** (`Ticket.ValuesOfTicketStatus`):
`ABERTO` → `PENDENTE` → `ACEITE` → `FINALIZADO` / `CANCELADO`

**Prioridade do Ticket** (`Ticket.ValuesOfPriority`):
`HIGH` | `MEDIUM` | `LOW`

---

## 5. Segurança e Autenticação

### 5.1 Mecanismo JWT (RS256)

- Algoritmo: **RSA 256** (chave pública/privada)
- Biblioteca: **JJWT 0.12.x** + **Nimbus JOSE**
- Sessão: **STATELESS** — nenhuma sessão HTTP é criada
- CSRF: **desabilitado** (desnecessário em APIs stateless com JWT)

### 5.2 Fluxo de Autenticação

```
Cliente                          API
  │                               │
  │  POST /auth/login             │
  │  { email, password }          │
  │──────────────────────────────►│
  │                               │  Valida credenciais + active=true
  │  { accessToken, refreshToken }│  Gera par de tokens JWT (RS256)
  │◄──────────────────────────────│  Persiste refreshToken em tb_refresh_tokens
  │                               │
  │  GET /qualquer-endpoint       │
  │  Authorization: Bearer <at>   │
  │──────────────────────────────►│
  │                               │  Decodifica JWT, extrai scopes
  │  200 OK                       │  Verifica @PreAuthorize
  │◄──────────────────────────────│
  │                               │
  │  POST /auth/refresh           │
  │  { refreshToken }             │
  │──────────────────────────────►│
  │                               │  Valida refreshToken no banco
  │  { novo accessToken, ... }    │  Rotaciona o par de tokens
  │◄──────────────────────────────│
```

### 5.3 Roles e Escopos

Os escopos são embutidos no claim `scope` do JWT como `SCOPE_<ROLE_NAME>`.

| Role | Escopo JWT | Descrição |
|---|---|---|
| `API_ADMIN` | `SCOPE_API_ADMIN` | Acesso total ao sistema |
| `API_BASIC` | `SCOPE_API_BASIC` | Acesso básico de leitura |
| `API_GROUP` | `SCOPE_API_GROUP` | Visualização de grupos |
| `API_GROUP_MANAGER` | `SCOPE_API_GROUP_MANAGER` | Gestão de grupos |
| `API_USER` | `SCOPE_API_USER` | Visualização de usuários |
| `API_USER_MANAGER` | `SCOPE_API_USER_MANAGER` | Gestão de usuários |
| `API_ROLE` | `SCOPE_API_ROLE` | Visualização de roles |
| `API_ROLE_MANAGER` | `SCOPE_API_ROLE_MANAGER` | Gestão de roles |
| `API_TICKET` | `SCOPE_API_TICKET` | Operações em tickets |
| `API_TICKET_MANAGER` | `SCOPE_API_TICKET_MANAGER` | Gestão completa de tickets |
| `API_TICKET_CATEGORY` | `SCOPE_API_TICKET_CATEGORY` | Visualização de categorias |
| `API_TICKET_CATEGORY_MANAGER` | `SCOPE_API_TICKET_CATEGORY_MANAGER` | Gestão de categorias |
| `API_TICKET_MESSAGE` | `SCOPE_API_TICKET_MESSAGE` | Mensagens em tickets |
| `API_TICKET_MESSAGE_MANAGER` | `SCOPE_API_TICKET_MESSAGE_MANAGER` | Gestão de mensagens |
| `API_TICKET_LOG` | `SCOPE_API_TICKET_LOG` | Visualização de logs |
| `API_TICKET_LOG_MANAGER` | `SCOPE_API_TICKET_LOG_MANAGER` | Gestão de logs |

> Novos usuários recebem automaticamente a role `API_BASIC` ao se cadastrar.
> O usuário `admin` recebe todas as roles na inicialização.

---

## 6. Endpoints da API

Base path: `/hdesktickets`

### 6.1 Autenticação — `/auth`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| POST | `/auth/login` | Público | Login com email/senha, retorna par de tokens |
| POST | `/auth/refresh` | Público | Renova tokens usando refreshToken |
| POST | `/auth/logout` | Público | Invalida o refreshToken |

### 6.2 Usuários — `/profiles-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/profiles-manager/users` | Público | Cria novo usuário (recebe `API_BASIC`) |
| GET | `/profiles-manager/users` | `API_ADMIN` | Lista usuários paginado |
| GET | `/profiles-manager/user/{id}/activate` | `API_ADMIN`, `API_USER_MANAGER` | Ativa usuário |
| GET | `/profiles-manager/user/{id}/deactivate` | `API_ADMIN`, `API_USER_MANAGER` | Desativa usuário |

### 6.3 Grupos — `/group-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/group-manager/groups` | `API_ADMIN`, `API_GROUP_MANAGER` | Cria grupo |
| PUT | `/group-manager/groups/{id}` | `API_ADMIN`, `API_GROUP_MANAGER` | Atualiza grupo |
| GET | `/group-manager/groups` | `API_ADMIN`, `API_GROUP_MANAGER` | Lista grupos paginado |
| GET | `/group-manager/groups/{id}` | `API_BASIC`, `API_GROUP` | Busca grupo por ID |
| PATCH | `/group-manager/groups/{id}/activate` | `API_ADMIN`, `API_GROUP_MANAGER` | Ativa grupo |
| PATCH | `/group-manager/groups/{id}/deactivate` | `API_ADMIN`, `API_GROUP_MANAGER` | Desativa grupo |
| POST | `/group-manager/groups/{id}/add-user/{userId}` | `API_ADMIN`, `API_GROUP_MANAGER` | Adiciona usuário ao grupo |
| DELETE | `/group-manager/groups/{id}/remove-user/{userId}` | `API_ADMIN`, `API_GROUP_MANAGER` | Remove usuário do grupo |
| GET | `/group-manager/groups/{id}/find-by-user` | `API_ADMIN`, `API_GROUP_MANAGER`, `API_GROUP` | Lista grupos de um usuário |

### 6.4 Roles — `/role-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| GET | `/role-manager/roles` | `API_ADMIN`, `API_ROLE_MANAGER` | Lista todas as roles |
| PATCH | `/role-manager/roles/{roleName}/user/{userId}` | `API_ADMIN`, `API_ROLE_MANAGER` | Adiciona role a usuário |
| DELETE | `/role-manager/roles/{roleName}/user/{userId}` | `API_ADMIN`, `API_ROLE_MANAGER` | Remove role de usuário |

### 6.5 Tickets — `/ticket-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/ticket-manager/tickets` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Cria ticket (status inicial: ABERTO) |
| GET | `/ticket-manager/tickets/dashboard` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Dashboard paginado de tickets |
| DELETE | `/ticket-manager/tickets/{id}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Cancela ticket (soft delete) |
| PUT | `/ticket-manager/tickets/{id}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Atualiza ticket completo |
| PATCH | `/ticket-manager/tickets/{id}/status/{status}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Atualiza status |
| PATCH | `/ticket-manager/tickets/{id}/priority/{priority}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Atualiza prioridade |
| PATCH | `/ticket-manager/tickets/{id}/attribuitedTo/{userId}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Atribui ticket a usuário |
| PATCH | `/ticket-manager/tickets/{id}/ticketCategory/{catId}` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Altera categoria |
| GET | `/ticket-manager/tickets/{id}/userId` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por usuário criador |
| GET | `/ticket-manager/tickets/{id}/attribuitedTo` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets atribuídos a usuário |
| GET | `/ticket-manager/tickets/{id}/ticketCategoryId` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por categoria |
| GET | `/ticket-manager/tickets/{status}/status` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por status |
| GET | `/ticket-manager/tickets/{priority}/priority` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por prioridade |
| GET | `/ticket-manager/tickets/{title}/title` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por título (contains) |
| GET | `/ticket-manager/tickets/{description}/description` | `API_ADMIN`, `API_TICKET_MANAGER`, `API_TICKET` | Tickets por descrição (contains) |

### 6.6 Categorias de Ticket — `/ticket-category-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/ticket-category-manager/ticket-categories` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Cria categoria |
| PUT | `/ticket-category-manager/ticket-categories/{id}` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Atualiza categoria |
| DELETE | `/ticket-category-manager/ticket-categories/{id}` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Remove categoria |
| GET | `/ticket-category-manager/ticket-categories/{id}` | `API_ADMIN`, `API_TICKET_CATEGORY` | Busca por ID |
| GET | `/ticket-category-manager/ticket-categories` | `API_ADMIN`, `API_TICKET_CATEGORY` | Lista paginado |
| PATCH | `/ticket-category-manager/ticket-categories/{id}/activate` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Ativa categoria |
| PATCH | `/ticket-category-manager/ticket-categories/{id}/deactivate` | `API_ADMIN`, `API_TICKET_CATEGORY_MANAGER` | Desativa categoria |

### 6.7 Mensagens de Ticket — `/ticket-message-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/ticket-message-manager/ticket-message` | `API_ADMIN`, `API_BASIC`, `API_TICKET_MESSAGE` | Cria mensagem |
| PUT | `/ticket-message-manager/ticket-message/{id}` | `API_ADMIN`, `API_TICKET_MESSAGE_MANAGER` | Remove mensagem |
| GET | `/ticket-message-manager/ticket-messages` | `API_ADMIN`, `API_BASIC`, `API_TICKET_MESSAGE`, `API_TICKET_MESSAGE_MANAGER` | Lista paginado |
| GET | `/ticket-message-manager/ticket-message/{id}` | `API_ADMIN`, `API_BASIC`, `API_TICKET_MESSAGE`, `API_TICKET_MESSAGE_MANAGER` | Busca por ID |
| GET | `/ticket-message-manager/ticket-message/{id}/ticket` | `API_ADMIN`, `API_BASIC`, `API_TICKET_MESSAGE`, `API_TICKET_MESSAGE_MANAGER` | Mensagens por ticket |
| GET | `/ticket-message-manager/ticket-message/{id}/user` | `API_ADMIN`, `API_BASIC`, `API_TICKET_MESSAGE`, `API_TICKET_MESSAGE_MANAGER` | Mensagens por usuário |

### 6.8 Logs de Ticket — `/ticket-log-manager`

| Método | Endpoint | Roles | Descrição |
|---|---|---|---|
| POST | `/ticket-log-manager/ticketlog` | `API_ADMIN`, `API_LOG_MANAGER` | Cria log manualmente |
| DELETE | `/ticket-log-manager/ticketlog/{id}` | `API_ADMIN`, `API_LOG_MANAGER` | Remove log |
| GET | `/ticket-log-manager/ticketlog/{id}` | `API_BASIC`, `API_LOG` | Busca log por ID |
| GET | `/ticket-log-manager/ticketlogs` | `API_BASIC`, `API_LOG` | Lista logs paginado |
| GET | `/ticket-log-manager/ticketlogs/{id}/ticket` | `API_BASIC`, `API_LOG` | Logs por ticket |
| GET | `/ticket-log-manager/ticketlogs/{id}/user` | `API_BASIC`, `API_LOG` | Logs por usuário |

---

## 7. Tratamento de Erros

Todas as exceções são tratadas centralmente por `ExceptionHandlerAdvice` e retornam o formato:

```json
{
  "timestamp": "2026-05-15T10:00:00.000+00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "errors": [
    {
      "code": "object.not.found",
      "message": "O recurso solicitado não foi encontrado."
    }
  ]
}
```

### Hierarquia de Exceções de Negócio

```
RuntimeException
└── BaseRuntimeException (implements MessageException)
    ├── ObjectNotFoundException          → 404 Not Found
    ├── ObjectNotProcessableException    → 422 Unprocessable Entity
    ├── ObjectNotActivatedException      → 422 Unprocessable Entity
    ├── LoginEmailOrPasswordException    → 401 Unauthorized
    ├── TokenExpiredException            → 401 Unauthorized
    ├── UserNotFoundException            → 404 Not Found
    └── UserNotAuthorizedException       → 403 Forbidden
```

As mensagens de erro são internacionalizadas via `MessageSource` (arquivos em `src/main/resources/messages/`).

---

## 8. Configurações por Ambiente

### Perfis Spring

| Perfil | Arquivo | Banco | Porta |
|---|---|---|---|
| `development` | `application-development.yml` | `localhost:5433/hdesktickets_db_dev` | 8080 |
| `test` | `application-test.yml` | `localhost:5434/hdesktickets_db_test` | — |
| `production` | `application-production.yml` | Configurado via env vars | 7000 |

### Configurações de desenvolvimento relevantes

- `ddl-auto: update` — Hibernate atualiza o schema automaticamente
- `show-sql: true` — Queries SQL exibidas no console
- `sql.init.mode: always` — `data.sql` executado a cada inicialização (insere roles)
- Spring Security em nível `DEBUG`

---

## 9. Infraestrutura Docker

O `docker-compose.yml` em `hdesktickets/` define dois serviços PostgreSQL:

```yaml
postgres_dev:   # porta 5433 → banco hdesktickets_db_dev
postgres_test:  # porta 5434 → banco hdesktickets_db_test
```

Ambos usam `image: postgres:latest` com usuário `hdesktickets` e senha `secret`.

---

## 10. Script de Build (`build.sh`)

O script `hdesktickets/scripts/build.sh` automatiza o ciclo completo:

1. Carrega variáveis do arquivo `.conf` correspondente ao ambiente
2. Verifica/sobe o container Docker do banco se necessário (aguarda porta ficar disponível)
3. Executa `mvnw clean package` com o perfil correto
4. Inicia a aplicação passando todas as variáveis como system properties Java (`-D`)

```bash
# Opções disponíveis
-e, --env <environment>   development | production | test  (padrão: development)
-a, --action <action>     build | run | build-run | test   (padrão: build-run)
-s, --skip-tests          Pula os testes no build
-h, --help                Exibe ajuda
```

---

## 11. Inicialização Automática

Na inicialização da aplicação (`CommandLineRunner`):

1. **`data.sql`** — Insere as 16 roles padrão em `tb_roles` (idempotente via `ON CONFLICT DO NOTHING`)
2. **`AdminUserConfig`** — Verifica se o usuário `admin` existe; se não, cria com todas as roles e senha codificada em BCrypt

---

## 12. Internacionalização (i18n)

- Configurado via `CustomConfig` com `ReloadableResourceBundleMessageSource`
- Arquivos de mensagens em `src/main/resources/messages/BusinessMessages*.properties`
- Locale alterável via parâmetro `?lang=` na requisição
- Mensagens de validação Bean Validation em `ValidationMessages.properties`

---

## 13. Documentação OpenAPI (Swagger)

Configurado em `SprigDocConfig`:

- Título: `Help Desk Tickets API v1.0`
- Esquema de segurança: `bearer-key` (HTTP Bearer JWT)
- Endpoints ordenados alfabeticamente (dev)
- Disponível em: `/hdesktickets/swagger-ui.html`

---

## 14. Regras de Negócio Relevantes

### Tickets
- Todo ticket criado inicia com status `ABERTO` e gera um `TicketLog` automaticamente
- "Deletar" um ticket é um soft delete: muda status para `CANCELADO` e registra `finalizationDateTime`
- Apenas o autor, o usuário atribuído ou um admin pode atualizar um ticket
- Finalizar (`FINALIZADO`) exige que o ticket tenha um usuário atribuído e que seja esse usuário (ou admin) a finalizar
- Cancelar (`CANCELADO`) só pode ser feito pelo autor ou admin
- Toda alteração de status, prioridade, categoria ou atribuição gera um `TicketLog`

### Usuários
- Novos usuários recebem automaticamente a role `API_BASIC`
- Não é possível remover a role `API_ADMIN` de nenhum usuário via API
- Não é possível desativar um usuário admin
- Reset de senha define a senha para `Metro@2025` (codificada em BCrypt)

### Grupos e Categorias
- Cada `TicketCategory` tem um `destinationGroup` — o grupo responsável por atender aquela categoria
- A autorização de operações em tickets verifica se o usuário pertence ao grupo de destino da categoria

---

## 15. Pontos de Atenção e Débitos Técnicos

| Item | Descrição |
|---|---|
| `TODO` em `Group.java` | Relacionamento `@ManyToMany` com `User` comentado por problema de `ConcurrentModificationException` |
| Validação de e-mail | Não implementada — usuários são criados sem confirmação de e-mail |
| HATEOAS | Dependência incluída (`spring-boot-starter-hateoas`) mas não implementada nos responses |
| Testes | Sem cobertura de testes unitários ou de integração |
| CORS | Configurado para aceitar qualquer origem (`*`) — restringir em produção |
| Senha de reset hardcoded | `passwordReset` define senha fixa `Metro@2025` — deve ser gerada aleatoriamente |
| `@Serial` ausente em `TicketMessage` | Entidade não declara `serialVersionUID` |
