/**
 * Representa o Refresh Token persistido para um usuário.
 *
 * <p>O fluxo de autenticação usa dois tokens JWT:
 * <ul>
 *   <li><b>Access Token</b>: vida curta (padrão 60s), carrega os escopos/roles do usuário.</li>
 *   <li><b>Refresh Token</b>: vida longa (padrão 900s), usado exclusivamente para renovar o par de tokens.</li>
 * </ul>
 *
 * <p>O campo {@code token} armazena um UUID aleatório que é embutido no claim {@code scope}
 * do JWT de refresh. Na renovação, esse UUID é extraído do JWT e comparado com o registro
 * no banco para garantir que o token não foi revogado (logout) ou substituído.
 *
 * <p>Cada usuário possui no máximo um refresh token ativo — o registro anterior é deletado
 * a cada login ou renovação bem-sucedida.
 *
 * @see br.com.doubletelecom.help_desk_tickets.app.security.JWTUtils
 * @see br.com.doubletelecom.help_desk_tickets.app.services.RefreshTokenService
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_refresh_tokens")
public class RefreshToken implements Serializable{

    /** Identificador interno do registro no banco. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refreshTokenId;

    /**
     * UUID aleatório embutido no claim {@code scope} do JWT de refresh.
     * Usado para validar e revogar o token sem precisar decodificar o JWT completo.
     */
    private UUID token;

    /** Instante de expiração do refresh token. */
    private Instant expiresAt;

    /** Usuário proprietário deste refresh token. Relação 1:1. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
