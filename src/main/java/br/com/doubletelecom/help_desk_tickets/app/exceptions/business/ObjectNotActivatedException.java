/**
 * Exception thrown when an operation is attempted on an object that is not activated.
 * This exception results in a HTTP 400 Bad Request response.
 * 
 * <p>This exception extends {@link BaseRuntimeException} and provides a specific
 * exception key {@code "object.not.activated"} for localization or error handling purposes.</p>
 * 
 * @see BaseRuntimeException
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectNotActivatedException extends BaseRuntimeException {
    private static final String KEY = "object.not.activated";

    public ObjectNotActivatedException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }

}
