/**
 * Exception thrown when there is an issue with the login email or password.
 * This exception is mapped to a HTTP 400 Bad Request status.
 * 
 * <p>This exception extends {@link BaseRuntimeException} and provides a specific
 * exception key for localization purposes.</p>
 * 
 * @see BaseRuntimeException
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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

