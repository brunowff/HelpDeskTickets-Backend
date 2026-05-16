# HelpDeskTickets

API RESTful para administração de chamados de suporte técnico (Help Desk), com autenticação stateless via JWT (RS256) e controle de acesso baseado em roles (RBAC).

---

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Setup local (desenvolvimento)](#setup-local-desenvolvimento)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Build e execução](#build-e-execução)
- [Acesso e autenticação](#acesso-e-autenticação)
- [CI/CD com GitHub Actions](#cicd-com-github-actions)
- [Segurança — o que não vai ao repositório](#segurança--o-que-não-vai-ao-repositório)
- [Documentação técnica](#documentação-técnica)
- [Próximos passos](#próximos-passos)

---

## Pré-requisitos

| Ferramenta | Versão mínima | Observação |
|---|---|---|
| Java | 25 | Recomendado via [SDKMAN](https://sdkman.io) |
| Maven | 3.9+ | Ou use o `./mvnw` incluso no projeto |
| Docker + Docker Compose | qualquer versão recente | Para subir o banco localmente |
| Lombok plugin | — | VS Code: `vscjava.vscode-lombok` |

---

## Setup local (desenvolvimento)

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd HelpDeskTickets
```

### 2. Subir o banco de dados

O projeto usa PostgreSQL via Docker Compose. Para desenvolvimento:

```bash
docker compose -f hdesktickets/docker-compose.yml up -d postgres_dev
```

Isso sobe um PostgreSQL na porta **5433** com o banco `hdesktickets_db_dev`.

Para testes:

```bash
docker compose -f hdesktickets/docker-compose.yml up -d postgres_test
```

Porta **5434**, banco `hdesktickets_db_test`.

### 3. Chaves RSA

As chaves de desenvolvimento já estão em `src/main/resources/dev_keys/` e **não são commitadas** (estão no `.gitignore`). Elas são necessárias para assinar e validar os tokens JWT localmente.

Se precisar gerar um novo par:

```bash
# Gerar chave privada RSA 2048 bits
openssl genrsa -out src/main/resources/dev_keys/private.key 2048

# Extrair a chave pública
openssl rsa -in src/main/resources/dev_keys/private.key \
            -pubout -out src/main/resources/dev_keys/public.key
```

### 4. Arquivo de configuração local

Os scripts de build carregam variáveis de um arquivo `.conf`. Copie o template e preencha:

```bash
# Para desenvolvimento (valores já preenchidos no template)
cp hdesktickets/scripts/hdesktickets-development.conf.example \
   hdesktickets/scripts/hdesktickets-development.conf

# Para produção local
cp hdesktickets/scripts/hdesktickets-production.conf.example \
   hdesktickets/scripts/hdesktickets-production.conf
```

> Os arquivos `.conf` sem `.example` estão no `.gitignore` — nunca são commitados.

---

## Variáveis de ambiente

Todas as variáveis são prefixadas com `HDT_`. O script `build.sh` as carrega do arquivo `.conf` correspondente ao ambiente e as passa como system properties Java (`-D`).

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `HDT_API_PROFILE` | Perfil Spring ativo: `development`, `test`, `production` | `development` |
| `HDT_PORT` | Porta HTTP da aplicação | `8080` |
| `HDT_DATASOURCE_URL` | Host e banco sem prefixo `jdbc:postgresql://` | `localhost:5433/hdesktickets_db_dev` |
| `HDT_DATASOURCE_USERNAME` | Usuário do PostgreSQL | `hdesktickets` |
| `HDT_DATASOURCE_PASSWORD` | Senha do PostgreSQL | `secret` |
| `HDT_API_RSA_PUBLIC_KEY` | Caminho da chave pública RSA | `classpath:dev_keys/public.key` |
| `HDT_API_RSA_PRIVATE_KEY` | Caminho da chave privada RSA | `classpath:dev_keys/private.key` |
| `HDT_API_TOKEN_ACCESS_TTL` | Validade do access token em segundos | `60` |
| `HDT_API_TOKEN_REFRESH_TTL` | Validade do refresh token em segundos | `900` |

> Em produção, `HDT_API_RSA_PUBLIC_KEY` e `HDT_API_RSA_PRIVATE_KEY` devem apontar para arquivos no sistema de arquivos do servidor, ex: `file:/opt/hdesktickets/key/private.key`.

---

## Build e execução

Use o script `hdesktickets/scripts/build.sh`:

```bash
# Build + run em desenvolvimento (padrão)
./hdesktickets/scripts/build.sh

# Apenas build, pulando testes
./hdesktickets/scripts/build.sh -s

# Executar testes
./hdesktickets/scripts/build.sh -e test -a test

# Apenas build para produção
./hdesktickets/scripts/build.sh -e production -a build

# Rodar JAR já compilado em produção
./hdesktickets/scripts/build.sh -e production -a run
```

Opções disponíveis:

| Flag | Valores | Padrão | Descrição |
|---|---|---|---|
| `-e`, `--env` | `development`, `test`, `production` | `development` | Ambiente alvo |
| `-a`, `--action` | `build`, `run`, `build-run`, `test` | `build-run` | Ação a executar |
| `-s`, `--skip-tests` | — | `false` | Pula os testes no build |
| `-h`, `--help` | — | — | Exibe ajuda |

Ou diretamente com Maven:

```bash
./mvnw clean package -DskipTests -Dspring.profiles.active=development
java -DHDT_API_PROFILE=development \
     -DHDT_DATASOURCE_URL=localhost:5433/hdesktickets_db_dev \
     -DHDT_DATASOURCE_USERNAME=hdesktickets \
     -DHDT_DATASOURCE_PASSWORD=secret \
     -DHDT_API_RSA_PUBLIC_KEY=classpath:dev_keys/public.key \
     -DHDT_API_RSA_PRIVATE_KEY=classpath:dev_keys/private.key \
     -jar target/help_desk_tickets-0.0.1-SNAPSHOT.jar
```

---

## Acesso e autenticação

Após iniciar a aplicação:

| Recurso | URL |
|---|---|
| API Base | `http://localhost:8080/hdesktickets` |
| Swagger UI | `http://localhost:8080/hdesktickets/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/hdesktickets/v3/api-docs` |

> Swagger está **desabilitado** no perfil `production`.

### Usuário admin padrão

Criado automaticamente na primeira inicialização:

| Campo | Valor |
|---|---|
| Email | `admin@youremail.com.br` |
| Senha | `Y0urP@$$worD` |

### Fluxo de autenticação JWT

```
1. POST /hdesktickets/auth/login
   Body: { "email": "...", "password": "..." }
   → Retorna accessToken (curta duração) + refreshToken (longa duração)

2. Todas as requisições autenticadas:
   Header: Authorization: Bearer <accessToken>

3. POST /hdesktickets/auth/refresh
   Body: { "refreshToken": "..." }
   → Renova o par de tokens (rotação automática)

4. POST /hdesktickets/auth/logout
   Body: { "refreshToken": "..." }
   → Invalida o refreshToken no banco
```

---

## CI/CD com GitHub Actions

O pipeline está em `.github/workflows/ci.yml` e executa automaticamente em:
- Push para `main` ou `dev`
- Pull Requests para `main`

### O que o pipeline faz

```
1. Checkout do código
2. Configura Java 25 (Temurin) com cache Maven
3. Decodifica as chaves RSA dos Secrets → arquivos temporários
4. Sobe PostgreSQL como service container (porta 5434)
5. Executa ./mvnw clean verify com perfil test
6. Faz upload do JAR como artefato (retido por 7 dias)
7. Remove as chaves RSA temporárias do runner (sempre, mesmo em falha)
```

### Cadastrar os GitHub Secrets

Acesse: **GitHub → repositório → Settings → Secrets and variables → Actions → New repository secret**

| Secret | Descrição | Como obter |
|---|---|---|
| `DB_USERNAME` | Usuário do banco de dados de teste | Defina um valor, ex: `hdesktickets` |
| `DB_PASSWORD` | Senha do banco de dados de teste | Defina uma senha segura |
| `RSA_PUBLIC_KEY_B64` | Chave pública RSA em Base64 | Ver comando abaixo |
| `RSA_PRIVATE_KEY_B64` | Chave privada RSA em Base64 | Ver comando abaixo |
| `TOKEN_ACCESS_TTL` | Validade do access token (segundos) | Ex: `300` |
| `TOKEN_REFRESH_TTL` | Validade do refresh token (segundos) | Ex: `3600` |

### Gerar os valores Base64 das chaves RSA

As chaves precisam ser convertidas para Base64 de uma linha para caber em um Secret:

```bash
# Chave pública
base64 -w 0 src/main/resources/dev_keys/public.key

# Chave privada
base64 -w 0 src/main/resources/dev_keys/private.key
```

Cole o output de cada comando diretamente no campo **Value** do Secret correspondente.

> No macOS, use `base64 -i` sem o `-w 0` (o macOS não tem a flag `-w`):
> ```bash
> base64 -i src/main/resources/dev_keys/public.key | tr -d '\n'
> base64 -i src/main/resources/dev_keys/private.key | tr -d '\n'
> ```

### Como o pipeline usa as chaves

O step `Restaurar chaves RSA` decodifica os Secrets e grava os arquivos em `src/main/resources/dev_keys/` dentro do runner — que é um ambiente efêmero e descartado após o job. O step final `Limpar chaves RSA temporárias` remove os arquivos explicitamente com `if: always()`, garantindo a limpeza mesmo em caso de falha.

---

## Segurança — o que não vai ao repositório

Os arquivos abaixo estão no `.gitignore` e **nunca devem ser commitados**:

| Arquivo / Padrão | Motivo |
|---|---|
| `src/main/resources/dev_keys/*.key` | Chaves RSA privada e pública |
| `hdesktickets/scripts/hdesktickets.conf` | Credenciais de produção |
| `hdesktickets/scripts/hdesktickets-production.conf` | Credenciais de produção |
| `*.env`, `.env.*` | Variáveis de ambiente com credenciais |
| `*.pem`, `*.p12`, `*.jks` | Certificados e keystores |

Use os arquivos `.example` como referência para criar as versões locais:

```bash
ls hdesktickets/scripts/*.example
```

---

## Documentação técnica

Para detalhes de arquitetura, modelo de dados, endpoints completos, hierarquia de exceções e decisões de design, consulte o [TEC-README.md](TEC-README.md).

---

## Próximos passos

- Validação de e-mail para novos usuários
- Implementação completa de HATEOAS (nível 3 Richardson)
- Aplicar padrão Strategy para regras de negócio (SOLID)
- Cobertura de testes unitários e de integração
- Pipeline de deploy contínuo (CD) para ambiente de produção
