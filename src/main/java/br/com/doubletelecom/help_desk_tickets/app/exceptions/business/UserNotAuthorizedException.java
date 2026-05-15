/**
 * Exception thrown when an authenticated user attempts an action they don't have permission for.
 *
 * <p>Uses HTTP 403 Forbidden — the correct status for authorization failures.
 * HTTP 401 Unauthorized is reserved for missing/invalid authentication credentials.</p>
 *
 * @see BaseRuntimeException
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotAuthorizedException extends BaseRuntimeException{
    private static final String KEY = "user.not.authorized";

    public UserNotAuthorizedException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
