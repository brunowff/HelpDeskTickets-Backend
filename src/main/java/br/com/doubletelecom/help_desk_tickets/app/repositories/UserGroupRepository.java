package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
import java.util.List;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    public List<User> findUsersByGroup(Group group);
    public List<Group> findGroupsByUser(User user);
    public List<UserGroup> findByGroup(Group group);
    public UserGroup findByUserAndGroup(User user, Group group);
}
