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
