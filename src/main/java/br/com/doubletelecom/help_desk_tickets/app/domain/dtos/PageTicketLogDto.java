package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.List;

public record PageTicketLogDto(List<PageItemTicketLogDto> pageItems,
                        int page,
                        int pageSize,
                        int totalPages,
                        Long totalElements) {

}
