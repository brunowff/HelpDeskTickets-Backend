/**
 * DTO de resposta para um item de mensagem de ticket em listagens paginadas.
 *
 * <p>Usa {@link UserDto} em vez de expor a entidade {@link User} diretamente,
 * evitando vazar campos sensíveis (como senha) na resposta da API.
 * Expõe apenas o UUID do ticket em vez da entidade completa.</p>
 *
 * @param ticketMessageId  identificador único da mensagem
 * @param ticketId         UUID do ticket associado
 * @param user             dados públicos do usuário que enviou a mensagem
 * @param message          conteúdo da mensagem
 * @param creationDateTime instante em que a mensagem foi criada
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.time.Instant;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;

public record PageItemTicketMessageDto(
    UUID ticketMessageId,
    UUID ticketId,
    UserDto user,
    String message,
    Instant creationDateTime
) {
    public PageItemTicketMessageDto(TicketMessage ticketMessage) {
        this(
            ticketMessage.getTicketMessageId(),
            ticketMessage.getTicket().getTicketId(),
            new UserDto(ticketMessage.getUser()),
            ticketMessage.getMessage(),
            ticketMessage.getMessageDateTime()
        );
    }
}
