/**
 * DTO de resposta para um item de log de ticket em listagens paginadas.
 *
 * <p>Usa {@link UserDto} em vez de expor a entidade {@link User} diretamente,
 * evitando vazar campos sensíveis (como senha) na resposta da API.</p>
 *
 * @param ticketLogId       identificador único do log
 * @param ticketId          UUID do ticket associado (evita serializar a entidade inteira)
 * @param user              dados públicos do usuário que gerou o log
 * @param description       descrição da ação registrada
 * @param creationDateTime  instante em que o log foi criado
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.time.Instant;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;

public record PageItemTicketLogDto(
    UUID ticketLogId,
    UUID ticketId,
    UserDto user,
    String description,
    Instant creationDateTime
) {
    public PageItemTicketLogDto(TicketLog ticketLog) {
        this(
            ticketLog.getTicketLogId(),
            ticketLog.getTicket().getTicketId(),
            new UserDto(ticketLog.getUser()),
            ticketLog.getLogDescription(),
            ticketLog.getLogDateTime()
        );
    }
}
