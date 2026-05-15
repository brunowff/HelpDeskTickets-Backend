#!/bin/bash

# =============================================================================
# HelpDesk Tickets - Build & Run Script
# Usage: ./build.sh [OPTIONS]
#
# Options:
#   -e, --env <environment>   Target environment: development | production | test
#                             Default: development
#   -a, --action <action>     Action to perform: build | run | build-run | test
#                             Default: build-run
#   -s, --skip-tests          Skip tests during build
#   -h, --help                Show this help message
#
# Examples:
#   ./build.sh                              # build + run in development
#   ./build.sh -e production -a run        # run in production (jar ja compilado)
#   ./build.sh -e test -a test             # executa os testes
#   ./build.sh -e production -a build      # apenas build para production
#   ./build.sh -e development -s           # build + run em dev sem rodar testes
# =============================================================================

set -euo pipefail

# --- Defaults -----------------------------------------------------------------
ENVIRONMENT="development"
ACTION="build-run"
SKIP_TESTS=false
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
DOCKER_COMPOSE_FILE="$SCRIPT_DIR/../docker-compose.yml"

# Mapeamento ambiente -> service do docker-compose -> porta do banco
declare -A DB_SERVICE=( [development]="postgres_dev" [test]="postgres_test" [production]="" )
declare -A DB_PORT=( [development]="5433" [test]="5434" [production]="" )

# --- Colors -------------------------------------------------------------------
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
log_success() { echo -e "${GREEN}[OK]${NC}    $*"; }
log_warn()    { echo -e "${YELLOW}[WARN]${NC}  $*"; }
log_error()   { echo -e "${RED}[ERROR]${NC} $*" >&2; }

# --- Help ---------------------------------------------------------------------
show_help() {
    sed -n '/^# Usage/,/^# =====/p' "$0" | grep '^#' | sed 's/^# \?//'
    exit 0
}

# --- Parse arguments ----------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        -e|--env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -a|--action)
            ACTION="$2"
            shift 2
            ;;
        -s|--skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        -h|--help)
            show_help
            ;;
        *)
            log_error "Opcao desconhecida: $1"
            show_help
            ;;
    esac
done

# --- Validate environment -----------------------------------------------------
case "$ENVIRONMENT" in
    development|production|test) ;;
    *)
        log_error "Ambiente invalido: '$ENVIRONMENT'. Use: development | production | test"
        exit 1
        ;;
esac

# --- Validate action ----------------------------------------------------------
case "$ACTION" in
    build|run|build-run|test) ;;
    *)
        log_error "Acao invalida: '$ACTION'. Use: build | run | build-run | test"
        exit 1
        ;;
esac

# --- Load environment config --------------------------------------------------
load_env_config() {
    local env_conf="$SCRIPT_DIR/hdesktickets.conf"
    local env_specific_conf="$SCRIPT_DIR/hdesktickets-${ENVIRONMENT}.conf"

    if [[ -f "$env_conf" ]]; then
        log_info "Carregando configuracao base: hdesktickets.conf"
        source "$env_conf"
    fi

    if [[ -f "$env_specific_conf" ]]; then
        log_info "Carregando configuracao do ambiente: hdesktickets-${ENVIRONMENT}.conf"
        source "$env_specific_conf"
    elif [[ ! -f "$env_conf" ]]; then
        log_warn "Nenhum arquivo de configuracao encontrado. Usando variaveis de ambiente do sistema."
    fi

    HDT_API_PROFILE="$ENVIRONMENT"
    HDT_PORT="${HDT_PORT:-8080}"
    HDT_JAR="${HDT_JAR:-target/help_desk_tickets-0.0.1-SNAPSHOT.jar}"
    HDT_API_TOKEN_ACCESS_TTL="${HDT_API_TOKEN_ACCESS_TTL:-60}"
    HDT_API_TOKEN_REFRESH_TTL="${HDT_API_TOKEN_REFRESH_TTL:-900}"
}

# --- Print config summary -----------------------------------------------------
print_config() {
    echo ""
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  HelpDesk Tickets - $ENVIRONMENT${NC}"
    echo -e "${BLUE}========================================${NC}"
    log_info "Ambiente:       $HDT_API_PROFILE"
    log_info "Porta:          $HDT_PORT"
    log_info "Datasource:     $HDT_DATASOURCE_URL"
    log_info "DB User:        $HDT_DATASOURCE_USERNAME"
    log_info "DB Password:    ****"
    log_info "Public Key:     $HDT_API_RSA_PUBLIC_KEY"
    log_info "Private Key:    $HDT_API_RSA_PRIVATE_KEY"
    log_info "Token Access:   ${HDT_API_TOKEN_ACCESS_TTL}s"
    log_info "Token Refresh:  ${HDT_API_TOKEN_REFRESH_TTL}s"
    log_info "JAR:            $HDT_JAR"
    echo ""
}

# --- Database check & start ---------------------------------------------------
ensure_database() {
    # production nao usa docker-compose gerenciado por este script
    if [[ "$ENVIRONMENT" == "production" ]]; then
        log_info "Ambiente production: gerenciamento do banco e externo, pulando verificacao Docker."
        return 0
    fi

    local service="${DB_SERVICE[$ENVIRONMENT]}"
    local port="${DB_PORT[$ENVIRONMENT]}"

    if [[ ! -f "$DOCKER_COMPOSE_FILE" ]]; then
        log_warn "docker-compose.yml nao encontrado em: $DOCKER_COMPOSE_FILE"
        log_warn "Certifique-se de que o banco esta acessivel em localhost:$port"
        return 0
    fi

    # Verifica se a porta do banco esta respondendo (banco ja esta up)
    if nc -z localhost "$port" 2>/dev/null; then
        log_success "Banco de dados ja esta rodando na porta $port."
        return 0
    fi

    log_warn "Banco de dados nao encontrado na porta $port."
    log_info "Subindo container Docker: $service ..."

    docker compose -f "$DOCKER_COMPOSE_FILE" up -d "$service"

    # Aguarda o banco aceitar conexoes (timeout de 30s)
    log_info "Aguardando o banco ficar disponivel (timeout: 30s)..."
    local attempts=0
    local max_attempts=30
    until nc -z localhost "$port" 2>/dev/null; do
        attempts=$((attempts + 1))
        if [[ $attempts -ge $max_attempts ]]; then
            log_error "Timeout: banco nao ficou disponivel em ${max_attempts}s na porta $port."
            log_error "Verifique os logs com: docker compose -f $DOCKER_COMPOSE_FILE logs $service"
            exit 1
        fi
        sleep 1
        printf "."
    done
    echo ""
    log_success "Banco de dados disponivel na porta $port."
}

# --- Build --------------------------------------------------------------------
do_build() {
    log_info "Iniciando build Maven para o ambiente: $ENVIRONMENT"

    local mvn_cmd="./mvnw clean package -Dspring.profiles.active=$ENVIRONMENT"

    if [[ "$SKIP_TESTS" == true ]]; then
        mvn_cmd="$mvn_cmd -DskipTests"
        log_warn "Testes ignorados (--skip-tests)"
    fi

    log_info "Executando: $mvn_cmd"
    cd "$PROJECT_ROOT"
    eval "$mvn_cmd"

    log_success "Build concluido: $HDT_JAR"
}

# --- Test ---------------------------------------------------------------------
do_test() {
    log_info "Executando testes para o ambiente: test"
    cd "$PROJECT_ROOT"
    ./mvnw test -Dspring.profiles.active=test
    log_success "Testes concluidos."
}

# --- Run ----------------------------------------------------------------------
do_run() {
    if [[ ! -f "$PROJECT_ROOT/$HDT_JAR" ]]; then
        log_error "JAR nao encontrado: $PROJECT_ROOT/$HDT_JAR"
        log_error "Execute primeiro com a acao 'build' ou 'build-run'."
        exit 1
    fi

    log_info "Iniciando aplicacao..."
    echo ""
    log_success "Acesse a aplicacao em:  http://localhost:$HDT_PORT/hdesktickets"
    log_success "Swagger UI:             http://localhost:$HDT_PORT/hdesktickets/swagger-ui.html"
    log_success "OpenAPI JSON:           http://localhost:$HDT_PORT/hdesktickets/v3/api-docs"
    echo ""

    cd "$PROJECT_ROOT"
    java \
        -DHDT_API_PROFILE="$HDT_API_PROFILE" \
        -DHDT_DATASOURCE_URL="$HDT_DATASOURCE_URL" \
        -DHDT_DATASOURCE_USERNAME="$HDT_DATASOURCE_USERNAME" \
        -DHDT_DATASOURCE_PASSWORD="$HDT_DATASOURCE_PASSWORD" \
        -DHDT_API_RSA_PUBLIC_KEY="$HDT_API_RSA_PUBLIC_KEY" \
        -DHDT_API_RSA_PRIVATE_KEY="$HDT_API_RSA_PRIVATE_KEY" \
        -DHDT_PORT="$HDT_PORT" \
        -DHDT_API_TOKEN_ACCESS_TTL="$HDT_API_TOKEN_ACCESS_TTL" \
        -DHDT_API_TOKEN_REFRESH_TTL="$HDT_API_TOKEN_REFRESH_TTL" \
        -jar "$HDT_JAR"
}

# --- Main ---------------------------------------------------------------------
load_env_config
print_config

case "$ACTION" in
    build)
        do_build
        ;;
    run)
        ensure_database
        do_run
        ;;
    build-run)
        do_build
        ensure_database
        do_run
        ;;
    test)
        ensure_database
        do_test
        ;;
esac
