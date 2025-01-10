/**
 * Exception thrown when an object cannot be processed.
 * 
 * <p>This exception is mapped to the HTTP status code 422 (Unprocessable Entity).</p>
 * 
 * <p>It extends from {@link BaseRuntimeException} and provides a specific key for 
 * identifying the exception type.</p>
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

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ObjectNotProcessableException extends BaseRuntimeException {
    private static final String KEY = "object.not.processable";

    public ObjectNotProcessableException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
