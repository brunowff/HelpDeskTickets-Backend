/**
 * Exception thrown when a user is not found in the system.
 * This exception is mapped to a 404 Not Found HTTP status code.
 * 
 * <p>This class extends {@link BaseRuntimeException} and provides a specific
 * exception key for user not found scenarios.</p>
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

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseRuntimeException {
    private static final String KEY = "user.not.found";

    public UserNotFoundException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
