# HelpDeskTickets

API RESTful para administração de chamados de suporte técnico (Help Desk), com autenticação stateless via JWT (RS256) e controle de acesso baseado em roles (RBAC).

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java | 25 |
| Maven | 3.9+ (ou use o `mvnw` incluso) |
| Docker + Docker Compose | qualquer versão recente |
| Lombok plugin | VS Code: `vscjava.vscode-lombok` |

---

## Configuração do ambiente

### 1. Subir o banco de dados

```bash
cd hdesktickets
docker compose up -d postgres_dev
```

Isso sobe um PostgreSQL na porta **5433** com o banco `hdesktickets_db_dev`.

### 2. Chaves RSA (desenvolvimento)

As chaves de desenvolvimento já estão em `src/main/resources/dev_keys/`. Não é necessário gerar novas para rodar localmente.

Para produção, gere um par RSA e configure as variáveis de ambiente (veja a seção abaixo).

### 3. Variáveis de ambiente

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `HDT_API_PROFILE` | Perfil ativo (`development`, `test`, `production`) | `development` |
| `HDT_PORT` | Porta da aplicação | `8080` |
| `HDT_DATASOURCE_URL` | URL do banco (sem `jdbc:postgresql://`) | `localhost:5433/hdesktickets_db_dev` |
| `HDT_DATASOURCE_USERNAME` | Usuário do banco | `hdesktickets` |
| `HDT_DATASOURCE_PASSWORD` | Senha do banco | `secret` |
| `HDT_API_RSA_PUBLIC_KEY` | Caminho da chave pública RSA | `classpath:dev_keys/public.key` |
| `HDT_API_RSA_PRIVATE_KEY` | Caminho da chave privada RSA | `classpath:dev_keys/private.key` |
| `HDT_API_TOKEN_ACCESS_TTL` | TTL do access token (segundos) | `60` |
| `HDT_API_TOKEN_REFRESH_TTL` | TTL do refresh token (segundos) | `900` |

---

## Build e execução

Use o script `hdesktickets/scripts/build.sh`:

```bash
# Build + run em desenvolvimento (padrão)
./hdesktickets/scripts/build.sh

# Apenas build, sem testes
./hdesktickets/scripts/build.sh -s

# Executar testes
./hdesktickets/scripts/build.sh -e test -a test

# Build para produção
./hdesktickets/scripts/build.sh -e production -a build

# Rodar jar já compilado em produção
./hdesktickets/scripts/build.sh -e production -a run
```

Ou diretamente com Maven:

```bash
./mvnw clean package -DskipTests
java -jar target/help_desk_tickets-0.0.1-SNAPSHOT.jar
```

---

## Acesso

Após iniciar a aplicação:

| Recurso | URL |
|---|---|
| API Base | `http://localhost:8080/hdesktickets` |
| Swagger UI | `http://localhost:8080/hdesktickets/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/hdesktickets/v3/api-docs` |

### Usuário admin padrão

| Campo | Valor |
|---|---|
| Email | `admin@doubletelecom.com.br` |
| Senha | `M3tr0T3l3c0m` |

> Criado automaticamente na primeira inicialização via `AdminUserConfig`.

---

## Autenticação

A API usa JWT com assinatura RS256. O fluxo é:

1. `POST /hdesktickets/auth/login` — retorna `accessToken` e `refreshToken`
2. Inclua o `accessToken` no header: `Authorization: Bearer <token>`
3. `POST /hdesktickets/auth/refresh` — renova os tokens usando o `refreshToken`
4. `POST /hdesktickets/auth/logout` — invalida o `refreshToken`

---

## Documentação técnica

Para detalhes de arquitetura, modelo de dados, endpoints completos e decisões de design, consulte o [TEC-README.md](TEC-README.md).

---

## Próximos passos

- Validação de e-mail para novos usuários
- Implementação completa de HATEOAS
- Atingir nível 3 do Modelo de Maturidade de Richardson
- Aplicar padrão Strategy para regras de negócio (SOLID)
- Pipeline de deploy (CI/CD)
- Cobertura de testes unitários e de integração
