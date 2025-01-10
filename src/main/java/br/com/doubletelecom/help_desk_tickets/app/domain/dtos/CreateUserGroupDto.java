/**
 * Data Transfer Object (DTO) para criação de um grupo de usuários.
 * Este DTO contém as informações necessárias para criar um grupo de usuários,
 * incluindo o ID do usuário e o ID do grupo.
 * 
 * Anotações:
 * 
 * - {@code @NotBlank}: Garante que o campo não seja nulo e não esteja vazio.</li>
 * 
 * 
 * Campos:
 * 
 * - {@code UUID userId}: O identificador único do usuário. Não deve estar em branco.</li>
 * - {@code UUID groupId}: O identificador único do grupo. Não deve estar em branco.</li>
 * 
 * 
 * Mensagens de Validação:
 * 
 * - {@code required.validation}: Mensagem a ser exibida se o campo estiver em branco.</li>
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CreateUserGroupDto(

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID groupId
) {

}
