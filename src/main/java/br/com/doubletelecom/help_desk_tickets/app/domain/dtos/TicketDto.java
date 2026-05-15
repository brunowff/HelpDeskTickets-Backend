/**
 * Data Transfer Object para atualização de um ticket existente.
 *
 * <p>Usa {@link Instant} para os campos de data/hora, consistente com as entidades JPA.
 * O campo {@code userAuthor} é somente leitura — o autor não pode ser alterado após a criação.</p>
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.time.Instant;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketDto(

    @NotNull(message = "{required.validation}")
    UUID ticketId,

    @NotBlank(message = "{required.validation}")
    String ticketTitle,

    @NotBlank(message = "{required.validation}")
    String ticketDescription,

    @NotBlank(message = "{required.validation}")
    String ticketStatus,
    
    @NotBlank(message = "{required.validation}")
    String ticketPriority,

    @NotNull(message = "{required.validation}")
    TicketCategory ticketCategory,

    @NotNull(message = "{required.validation}")
    UserDto userAuthor,

    UserDto attibuitedToUserId,

    Instant creationDateTime,

    Instant finalizationDateTime
    
    ) {

}
