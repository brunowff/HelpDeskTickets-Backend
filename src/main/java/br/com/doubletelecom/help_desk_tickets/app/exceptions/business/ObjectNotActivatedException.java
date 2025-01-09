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
