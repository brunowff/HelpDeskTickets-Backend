package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import java.util.List;


@Repository
public interface TicketLogRepository extends JpaRepository<TicketLog, UUID> {

    public List<TicketLog> findByTicketId(UUID ticket);
    public List<TicketLog> findByUserId(UUID user);

}
