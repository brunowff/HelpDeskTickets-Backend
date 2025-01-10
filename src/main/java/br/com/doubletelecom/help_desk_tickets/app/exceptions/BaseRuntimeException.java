/**
 * BaseRuntimeException is an abstract class that extends RuntimeException and implements the MessageException interface.
 * It serves as a base class for custom runtime exceptions in the application.
 * 
 * This class contains a map of details that can be used to provide additional information about the exception.
 * 
 * @param mapDetails a map containing additional details about the exception
 * 
 * @see java.lang.RuntimeException
 * @see br.com.doubletelecom.help_desk_tickets.app.exceptions.contract.MessageException
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions;

import java.util.Map;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.contract.MessageException;

public abstract class BaseRuntimeException extends RuntimeException implements MessageException {
    private final Map<String, Object> mapDetails;

    public BaseRuntimeException() {
        mapDetails = null;
    }
    public BaseRuntimeException(final Map<String, Object> mapDetails) {
        this.mapDetails = mapDetails;
    }

    public abstract String getExceptionKey();

    public Map<String, Object> getMapDetails() {
        return this.mapDetails;
    }
}
