/**
 * Data Transfer Object for a refresh token.
 * 
 * @param token The refresh token.
 * 
 * @author 
 * @version
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(@NotBlank String token) {
}
