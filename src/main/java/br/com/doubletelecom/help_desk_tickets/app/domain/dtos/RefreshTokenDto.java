/**
 * Data Transfer Object for a refresh token.
 * 
 * @param token The refresh token.
 * 
 * @author 
 * @version
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto(@NotNull JwtAuthenticationToken token) {
}
