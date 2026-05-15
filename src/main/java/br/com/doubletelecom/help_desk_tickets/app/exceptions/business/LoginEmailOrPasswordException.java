/**
 * Exception thrown when login fails due to invalid email or password.
 *
 * <p>Uses HTTP 401 Unauthorized — the correct status for failed authentication.
 * The message is intentionally generic to avoid revealing whether the email exists.</p>
 *
 * @see BaseRuntimeException
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginEmailOrPasswordException extends BaseRuntimeException {
    private static final String KEY = "login.email.or.password";

    public LoginEmailOrPasswordException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}

