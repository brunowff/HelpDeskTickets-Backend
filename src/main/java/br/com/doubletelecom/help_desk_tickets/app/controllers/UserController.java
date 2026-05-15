/**
 * Controller REST para gerenciamento de usuários.
 *
 * <p>Correções aplicadas:
 * <ul>
 *   <li>createUser: removido try/catch que engolia exceções reais (ex: email duplicado virava 422 genérico).</li>
 *   <li>activateUser / deactivateUser: trocado GET por PATCH — GET não deve causar efeitos colaterais (RFC 7231).</li>
 * </ul>
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/profiles-manager")
public class UserController {

    private final UserServices userServices;

    @PostMapping("/users")
    public ResponseEntity<PageItemUserDto> createUser(
            @RequestBody @Validated CreateUserDto userDto,
            UriComponentsBuilder uriBuilder) {
        // BUG FIX: exceções propagadas ao ExceptionHandlerAdvice — não engolir aqui
        var user = userServices.save(userDto);
        var uri = uriBuilder.path("/profiles-manager/users/{id}")
                .buildAndExpand(user.getUserId()).toUri();
        return ResponseEntity.created(uri).body(new PageItemUserDto(user));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN')")
    public ResponseEntity<Page<PageItemUserDto>> listUsers(
            @PageableDefault(page = 0, size = 10, sort = {"username"}) Pageable pageable) {
        var users = userServices.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    // BUG FIX: era GET — PATCH é o verbo correto para operações que alteram estado
    @PatchMapping("/user/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_USER_MANAGER')")
    public ResponseEntity<Void> activateUser(
            @PathVariable("id") String userId,
            JwtAuthenticationToken token) {
        userServices.activate(userId, token);
        return ResponseEntity.ok().build();
    }

    // BUG FIX: era GET — PATCH é o verbo correto para operações que alteram estado
    @PatchMapping("/user/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_USER_MANAGER')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable("id") String userId,
            JwtAuthenticationToken token) {
        userServices.deactivate(userId, token);
        return ResponseEntity.ok().build();
    }
}
