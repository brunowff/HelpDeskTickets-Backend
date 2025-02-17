
/**
 * Service implementation for managing users.
 * This class provides methods for user-related operations such as finding all users, saving a new user,
 * updating user information, adding/removing roles, resetting passwords, and activating/deactivating users.
 * 
 * Methods:
 * - findAll(Pageable pageable): Retrieves a paginated list of users.
 * - save(CreateUserDto userDto): Saves a new user.
 * - updateUser(UserDto userDto, JwtAuthenticationToken token): Updates user information.
 * - addRoleToUser(String userId, String roleName, JwtAuthenticationToken token): Adds a role to a user.
 * - removeRoleFromUser(String userId, String roleName, JwtAuthenticationToken token): Removes a role from a user.
 * - passwordReset(UserDto userDto, JwtAuthenticationToken token): Resets the password of a user.
 * - activate(String userId, JwtAuthenticationToken token): Activates a user.
 * - deactivate(String userId, JwtAuthenticationToken token): Deactivates a user.
 * 
 * Exceptions:
 * - ObjectNotFoundException: Thrown when an object is not found.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * - UserNotFoundException: Thrown when a user is not found.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service.
 * - @AllArgsConstructor: Generates a constructor with 1 parameter for each field in the class.
 * - @Transactional: Indicates that the method should be executed within a transaction.
 * - @RequestBody: Indicates that a method parameter should be bound to the body of the web request.
 * - @RequestParam: Indicates that a method parameter should be bound to a web request parameter.
 * - @Valid: Indicates that a method parameter should be validated.
 * 
 * Dependencies:
 * - UserRepository: Repository for managing user entities.
 * - RoleRepository: Repository for managing role entities.
 * - BCryptPasswordEncoder: Utility for encoding passwords.
 * 
 * @see UserServices
 * @see UserRepository
 * @see RoleRepository
 * @see BCryptPasswordEncoder
 * @see CreateUserDto
 * @see UserDto
 * @see PageItemUserDto
 * @see Role
 * @see User
 * @see ObjectNotFoundException
 * @see ObjectNotProcessableException
 * @see UserNotAuthorizedException
 * @see UserNotFoundException
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.UserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices{

    private final UserRepository userRep;
    private final RoleRepository roleRep;
    private final GroupRepository groupRep;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Optional<User> findByEmail(String email){  
        return userRep.findByEmail(email);
    }

    @Override
    @Transactional
    public Optional<User> findByUserId(UUID userId){  
        return userRep.findByUserId(userId);
    }

    @Override
    @Transactional
    public Page<PageItemUserDto> findAll(Pageable pageable){  
        return userRep.findAll(pageable).map(PageItemUserDto::new);
    }

    @Override
    @Transactional
    public User save(@RequestBody @Validated CreateUserDto userDto){
        
        var roleBasic = roleRep.findByName(Role.Values.API_BASIC.name()).orElse(null);
        var userFromDb = userRep.findByUsername(userDto.username());

        // Check if users already exist.
        if(userFromDb.isPresent()){
            throw new ObjectNotProcessableException();
        }

        var user = new User();
        
        user.setFullname(userDto.fullname());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        // Password encoding.
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setActive(true);
        user.setRoles(Set.of(roleBasic));

        return userRep.save(user);
    }

    @Override
    @Transactional
    public User updateUser(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
    
        if(user.isAdmin() || user.getUserId().equals(userDto.userId())){
            user.setFullname(userDto.fullname());
            user.setUsername(userDto.username());
            user.setEmail(userDto.email());
        return userRep.save(user);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public Void addRoleToUser(@RequestParam String userId, @RequestParam String roleName, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var user2alterate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ObjectNotFoundException());
        Role.Values roleEnumValues = Role.Values.valueOf(roleName);
        var role2add = roleRep.findByName(roleEnumValues.name()).orElseThrow( () -> new ObjectNotFoundException());
        
        if(user2alterate.hasRole(role2add.toString())){
            throw new ObjectNotProcessableException();
        }
        
        if(!user.isAdmin()){
            throw new UserNotAuthorizedException();
        }
        
        var roles = user2alterate.getRoles();
        roles.add(role2add);
        user2alterate.setRoles(roles);
        userRep.save(user2alterate);
        return null;
    }

    @Override
    @Transactional
    public Void removeRoleFromUser(@RequestParam String userId, @RequestParam String roleName, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(!user.isAdmin() || roleName.equals("API_ADMIN")){
            throw new UserNotAuthorizedException();
        }
        try {
            var role2remove = roleRep.findByName(roleName).orElseThrow( () -> new ObjectNotFoundException());
            var user2alterate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ObjectNotFoundException());
            var roles = user2alterate.getRoles();
            roles.remove(role2remove);
            user2alterate.setRoles(roles);
            userRep.save(user2alterate);
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }
        
        return null;
    }

    @Override
    @Transactional
    public Void passwordReset(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var user2update = userRep.findById(userDto.userId()).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin() || user.getUserId().equals(userDto.userId())){
            user2update.setPassword(passwordEncoder.encode("Metro@2025"));
            userRep.save(user2update);
        } else {
            throw new UserNotAuthorizedException();
        }

        return null;
    }

    @Override
    @Transactional
    public Void activate(@RequestParam String userId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var user2activate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin()){
            user2activate.setActive(true);
            userRep.save(user2activate);
        } else {
            throw new UserNotAuthorizedException();
        }

        return null;
    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String userId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var user2deactivate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin() && !user2deactivate.isAdmin()){
            user2deactivate.setActive(false);
            userRep.save(user2deactivate);
        } else {
            throw new UserNotAuthorizedException();
        }

        return null;
    }

    @Override
    @Transactional
    public Void addUserToGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(!user.isAdmin() && !user.hasRole("API_GROUP_MANAGER")){
            throw new UserNotAuthorizedException();
        }

        var group2add = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
        var user2add = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ObjectNotFoundException());
        var groups = user2add.getGroups();
        groups.add(group2add);
        user2add.setGroups(groups);
        userRep.save(user2add);

        return null;
    }

    @Override
    @Transactional
    public Void removeUserFromGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(!user.isAdmin()){
            throw new UserNotAuthorizedException();
        }
        
        try {
            var group2remove = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
            var user2alterate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ObjectNotFoundException());
            var groups = user2alterate.getGroups();
            groups.remove(group2remove);
            user2alterate.setGroups(groups);
            userRep.save(user2alterate);
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }
        
        return null;
    }

    @Override
    public List<PageItemGroupDto> findGroupsByUserId(String userId, JwtAuthenticationToken token){
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER") || user.hasRole("API_GROUP")){
            try {
                var user2find = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());
                Iterator<Group> groups = user2find.getGroups().iterator();
                List<PageItemGroupDto> groups2return = new CopyOnWriteArrayList<>();
                while (groups.hasNext()) {
                    groups2return.add(new PageItemGroupDto(groups.next()));
                }
                return groups2return;
            } catch (Exception e) {
                throw new ObjectNotFoundException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
        
    }
}
