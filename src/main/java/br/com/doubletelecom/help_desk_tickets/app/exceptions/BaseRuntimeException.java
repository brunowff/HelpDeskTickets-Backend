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
