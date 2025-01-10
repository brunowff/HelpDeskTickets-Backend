/**
 * Exception thrown when a user is not authorized to perform a certain action.
 * This exception results in a response with HTTP status 401 (Unauthorized).
 * 
 * <p>The exception key for this error is "user.not.authorized".</p>
 * 
 * <p>This class extends {@link BaseRuntimeException} and overrides the 
 * {@link BaseRuntimeException#getExceptionKey()} method to return the specific 
 * exception key for this error.</p>
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

@ResponseStatus(HttpStatus.UNAUTHORIZED)
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
