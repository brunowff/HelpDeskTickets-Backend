/**
 * Exception thrown when an object is not found.
 * This exception is mapped to the HTTP status code 404 (Not Found).
 * 
 * The exception key is "object.not.found".
 * 
 * Extends {@link BaseRuntimeException}.
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
public class ObjectNotFoundException extends BaseRuntimeException {
    private static final String KEY = "object.not.found";

    public ObjectNotFoundException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }

}
