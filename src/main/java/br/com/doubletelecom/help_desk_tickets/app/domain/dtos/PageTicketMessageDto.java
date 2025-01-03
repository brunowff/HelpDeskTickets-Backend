package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.List;

public record PageTicketMessageDto(List<PageItemTicketMessageDto> pageItems,
                        int page,
                        int pageSize,
                        int totalPages,
                        Long totalElements) {

}
