package br.com.doubletelecom.help_desk_tickets.app.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.RefreshToken;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    
    public Optional<RefreshToken> findByToken(UUID token);
    public void deleteByToken(UUID token);

}
